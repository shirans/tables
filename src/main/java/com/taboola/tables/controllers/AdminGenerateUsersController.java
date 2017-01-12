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

        /*for (int i = 0 ; i < 4 ; i ++) {
            String gmail =  new BigInteger(130, random).toString(32);
            User user = userRepo.findByMail(gmail);
            if (user == null) {
                userRepo.save(new User("user" + i, "gid +" + i, gmail));
            }
        }*/
        userRepo.save(new User("Eyal Segal", "user1", "eyal.s@taboola.com", "https://1e5b02e9b7ffeaf78626-c62e11221c1d1800fb58c14045a9dd1c.ssl.cf1.rackcdn.com/photos/40555-1-4.jpg"));
        userRepo.save(new User("Boaz Yaniv", "user2", "boaz.y@taboola.com", "https://lh3.googleusercontent.com/-O3qebDCTZFk/AAAAAAAAAAI/AAAAAAAAAAs/3C0QgLg0tJo/s96-c/photo.jpg"));
        userRepo.save(new User("Shiran Schwartz", "user3", "shiran.s@taboola.com", "https://1e5b02e9b7ffeaf78626-c62e11221c1d1800fb58c14045a9dd1c.ssl.cf1.rackcdn.com/photos/41186-1-4.jpg"));
        userRepo.save(new User("Rami Stern", "user4", "rami.s@taboola.com", "https://1e5b02e9b7ffeaf78626-c62e11221c1d1800fb58c14045a9dd1c.ssl.cf1.rackcdn.com/photos/41388-0-4.jpg"));

        return Lists.newArrayList(userRepo.findAll());
    }
}
