package com.taboola.tables.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/add-user-score")
    public HttpStatus addScore(@RequestParam(value="GmailId") String gmail, @RequestParam(value="score") double score) {
        final User byGmailId = userRepo.findByGmailId(gmail);
        if (byGmailId == null) {
            return HttpStatus.BAD_REQUEST;
        }
        double newScore = score + byGmailId.getScore();
        byGmailId.setScore(newScore);
        userRepo.save(byGmailId);
        return HttpStatus.OK;
    }
}
