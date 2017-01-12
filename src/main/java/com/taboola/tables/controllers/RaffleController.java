package com.taboola.tables.controllers;

import java.io.IOException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.taboola.tables.managers.CalendarManager;
import com.taboola.tables.managers.EmailManager;
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

    @Autowired
    private CalendarManager calendarManager;

    @Autowired
    private EmailManager emailManager;

    @RequestMapping("/admin/raffle")
    public HttpStatus getGroups(@RequestParam(value="numOfParticipates", defaultValue="4") int numOfParticipates,
                                @RequestParam(value="sendInvites", defaultValue="false") boolean sendInvites) {
        try{
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
            if (numOfGroups == 0) {
                return HttpStatus.BAD_REQUEST;
            }
            for (int i = 0; i < participatesList.size(); i++) {
                int currentGroup = (i % numOfGroups)+1;
                if (!groups.containsKey(currentGroup)){
                    groups.put(currentGroup, new ArrayList<>());
                }
                groups.get(currentGroup).add(participatesList.get(i));
            }

            final LocalDateTime nextTablesDate = getNextTablesDate();

            DateTime lunchTime = new DateTime(ZonedDateTime.now(ZoneId.of("Israel")).withHour(12).withMinute(30).withSecond(0).withNano(0).toInstant().getEpochSecond() * 1000);
            for (List<User> userList : groups.values()) {
                final Appointment appointment = new Appointment( "At Taboola Kitchen", nextTablesDate, userList);
                appoitnemntRepo.save(appointment);
                if (sendInvites) {
                    Event event = calendarManager.scheduleLunch(lunchTime, 60, userList, TimeZone.getTimeZone("Israel"));
                    emailManager.sendEmail(userList, createLunchEmail(userList, event));
                }
            }
            final Iterable<Appointment> appointments = appoitnemntRepo.findAll();
            return HttpStatus.OK;
        } catch (Exception e){
            logger.error("An error occurred", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        return appointments;
    }

    private LocalDateTime getNextTablesDate() {
        LocalDate nextWed = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        final LocalDateTime localDateTime = nextWed.atTime(12, 30);
        return localDateTime;
    }

    private String createLunchEmail(Collection<User> users, Event event) {
        return "<html><body>" +
                "<h2>Taboola Tables - Your weekly surprise lunch </h2>" +
                "<h3>You are scheduled to lunch with: </h3>" +
                users.stream().map(user -> "<table><tr><td><img src=\"" + user.getPicture() + "\" style=\"width:30px;height:30px;\" /></td><td>" + user.getName() + "</td></tr></table>").collect(Collectors.joining()) +
                "<div><a href='" + buildHangoutsLink(event) + "'>Talk about it!</a></div>" +
                "<br/>" +
                "<div><small><a href='http://tables.taboola.com'>tables.taboola.com</a></small></div>" +
                "</body></html>";
    }

    private static String buildHangoutsLink(Event event) {
        return String.format("https://plus.google.com/hangouts/_/taboola.com/taboola-tables?hceid=dGFibGVzQHRhYm9vbGEuY29t.%s&authuser=0", event.getId());
    }
}
