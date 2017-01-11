package com.taboola.tables.controllers;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.taboola.tables.db.Appointment;
import com.taboola.tables.db.AppointmentRepo;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class RaffleController {

    private static final Logger logger = LoggerFactory.getLogger(RaffleController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppointmentRepo appoitnemntRepo;

    @RequestMapping("/raffle")
    public HttpStatus getGroups(@RequestParam(value="numOfParticipates", defaultValue="4") int numOfParticipates, HttpServletResponse response) {
        if (numOfParticipates < 2 || numOfParticipates > 8) {
            logger.info("Num of participates is " +numOfParticipates+" shoud be between 3 to 8, setting to 4");
            numOfParticipates = 4;
        }
        logger.info("Num of participates is " +numOfParticipates);

        final Iterable<User> allUsers = userRepo.findAll();
        final ArrayList<User> participatesList = Lists.newArrayList(allUsers);
        Collections.shuffle(participatesList);

        int numOfGroups = participatesList.size() / numOfParticipates;
        final Map<Integer,List<User>> groups = new HashMap<>();
        for (int i = 0; i < participatesList.size(); i++) {
            int currentGroup = (i % numOfGroups)+1;
            if (!groups.containsKey(currentGroup)){
                groups.put(currentGroup, new ArrayList<>());
            }
            groups.get(currentGroup).add(participatesList.get(i));
        }

        final LocalDateTime nextTablesDate = getNextTablesDate();

        for (List<User> userList : groups.values()) {
            final Appointment appointment = new Appointment("At Taboola Kitchen", nextTablesDate, userList);
            appoitnemntRepo.save(appointment);
        }
        final Iterable<Appointment> appointments = appoitnemntRepo.findAll();
        return HttpStatus.OK;
//        return appointments;
    }

    private LocalDateTime getNextTablesDate() {
        LocalDate nextWed = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        final LocalDateTime localDateTime = nextWed.atTime(13, 0);
        return localDateTime;
    }
}
