package com.taboola.tables.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.taboola.tables.db.TaboolaIdentity;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserDataDirectory;
import com.taboola.tables.db.UserDataDirectoryRepo;
import com.taboola.tables.db.UserRepo;
import com.taboola.tables.db.UserSegmentData;
import com.taboola.tables.db.UserSegmentRepo;

/**
 * Created by eyal.s on 11/01/2017.
 */
@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserSegmentRepo userSegmentRepot;

    @Autowired
    UserDataDirectoryRepo userDataDirectoryRepo;

    @RequestMapping(value = "/singleUser", method = RequestMethod.GET)
    public User login(@RequestParam(name = "gmailId") String gmailId) {
        User byGmailId = userRepo.findByGmailId(gmailId);
        if (byGmailId != null) {
            return byGmailId;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public List<User> login() {
        return Lists.newArrayList(userRepo.findAll());
    }

    @RequestMapping("/segments")
    public List<UserDataDirectory> getSegments(@RequestParam(name = "gmailId") String gmailId) {
        try {
            User byGmailId = userRepo.findByGmailId(gmailId);
            if (byGmailId != null) {
                final List<TaboolaIdentity> taboolaIdentities = byGmailId.getTaboolaIdentities();
                if (taboolaIdentities != null) {
                    final List<UserSegmentData> userSegmentDataList = taboolaIdentities.stream().map(t -> userSegmentRepot.findByTid(t.getTaboolaId())).flatMap(List::stream).collect(Collectors.toList());
                    final List<UserDataDirectory> userDataDirectoryList = userSegmentDataList.stream().map(u -> userDataDirectoryRepo.findBySegmentId(u.getSegment())).collect(Collectors.toList());
                    return userDataDirectoryList;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }

    @RequestMapping(value = "/bulkInsert", method = RequestMethod.POST)
    public String bulkInsert(@RequestBody ArrayList<User> users){
        int newUsersCount = 0;

        for (User userInput : users){
            User user = userRepo.findByMail(userInput.getMail());
            if (user == null){
                userRepo.save(userInput);
                newUsersCount++;
            }
        }
        return "OK: " + newUsersCount;
    }

    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public Iterable<User> getAllUsers() {
        final Iterable<User> all = userRepo.findAll();
        return all;
    }
}
