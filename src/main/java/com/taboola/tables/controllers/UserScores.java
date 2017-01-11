package com.taboola.tables.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class UserScores {

    @Autowired
    private UserRepo userRepo;

    @RequestMapping("/add-user-score")
    public HttpStatus addScore(@RequestParam(value="gmailId") String gmail, @RequestParam(value="score", defaultValue = "1") double score) {
        final User byGmailId = userRepo.findByGmailId(gmail);
        if (byGmailId == null) {
            return HttpStatus.BAD_REQUEST;
        }
        double newScore = score + byGmailId.getScore();
        byGmailId.setScore(newScore);
        userRepo.save(byGmailId);
        return HttpStatus.OK;
    }

    @RequestMapping("/get-top-scored")
    public List<User> getTopScored(@RequestParam(value="top", defaultValue = "5") int top) {
        try{
            if (top <= 0) top = 5;
            final ArrayList<User> users = Lists.newArrayList(userRepo.findAll());
            users.sort((o1, o2) -> ((int)(o2.getScore() - o1.getScore())));
            if (top > users.size()) top = users.size();
            return users.subList(0, top);
        } catch (Exception e) {
            return null;
        }
    }
}
