package com.taboola.tables.controllers;

import com.google.api.client.util.Lists;
import com.taboola.tables.entities.PartialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyal.s on 11/01/2017.
 */
@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

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
