package com.taboola.tables.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.Application;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;
import com.taboola.tables.json.RaffleStatus;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class RaffleController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RaffleController.class);

    @Autowired
    UserRepo repository;

    @RequestMapping("/raffle")
    public Map<Integer,List<User>> getGroups(@RequestParam(value="numOfParticipates", defaultValue="4") int numOfParticipates) {
        if (numOfParticipates < 2 || numOfParticipates > 8) {
            logger.info("Num of participates is " +numOfParticipates+" shoud be between 3 to 8, setting to 4");
            numOfParticipates = 4;
        }
        logger.info("Num of participates is " +numOfParticipates);

        final List<User> participatesList = new ArrayList<>();//TODO take from DB
        final Iterable<User> allUsers = repository.findAll();
        allUsers.forEach(u -> participatesList.add(u));
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

        return groups;
    }
}
