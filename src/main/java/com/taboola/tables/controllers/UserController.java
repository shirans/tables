package com.taboola.tables.controllers;

import com.taboola.tables.entities.PartialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

import java.util.ArrayList;

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


    @RequestMapping(value = "/bulkInsert", method = RequestMethod.POST)
    public String bulkInsert(@RequestBody ArrayList<User> users){
        return "OK";
    }
}
