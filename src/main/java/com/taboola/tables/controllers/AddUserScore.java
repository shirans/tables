package com.taboola.tables.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class AddUserScore {

    @Autowired
    private UserRepo userRepo;

    @RequestMapping(value = "/add-user-score", method = RequestMethod.GET)
    public HttpStatus addScore(@RequestParam(value="mail") String mail, @RequestParam(value="appointmentId") int appointmentId) {
        final User user = userRepo.findByMail(mail);
        if (user == null) {
            return HttpStatus.BAD_REQUEST;
        }
        double newScore = user.getScore() + 1;
        user.setScore(newScore);
        userRepo.save(user);
        return HttpStatus.OK;
    }
}
