package com.taboola.tables.controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Lists;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * @author shiran.s on 1/12/17.
 */
@RestController
public class AdminGenerateUsersController {

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/admin/generateUsers", method = RequestMethod.GET)
    private List<User> setUpBasicUsers() {

        SecureRandom random = new SecureRandom();

        for (int i = 0 ; i < 4 ; i ++) {
            String gmail =  new BigInteger(130, random).toString(32);
            User user = userRepo.findByMail(gmail);
            if (user == null) {
                userRepo.save(new User("user" + i, "gid +" + i, gmail));
            }
        }
        return Lists.newArrayList(userRepo.findAll());
    }
}
