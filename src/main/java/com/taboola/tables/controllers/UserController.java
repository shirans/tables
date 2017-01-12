package com.taboola.tables.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.taboola.tables.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

/**
 * Created by eyal.s on 11/01/2017.
 */
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    private UserSegmentRepo userSegmentRepo;

    @Autowired
    private UserDataDirectoryRepo userDataDirectoryRepo;

    @RequestMapping(value = "/singleUser", method = RequestMethod.GET)
    public User login(@RequestParam(name = "gmailId") String gmailId) {
        User byGmailId = userRepo.findByGmailId(gmailId);
        if (byGmailId != null) {
            return byGmailId;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/getUserAppointmentByMail", method = RequestMethod.GET)
    public Appointment getUserByMail(@RequestParam(name = "mail") String mail) {

        User user = userRepo.findByMail(mail);
        final List<Appointment> appointments = user.getAppointments();
        appointments.sort((o1, o2) -> o1.getAppointmentDate().compareTo(o2.getAppointmentDate()));
        return appointments.get(0);
    }

    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public List<User> login() {
        return Lists.newArrayList(userRepo.findAll());
    }

    @RequestMapping(value = "/segments-all-users", method = RequestMethod.GET)
    public List<User> getAllUserSegments() {
        try {
            final List<User> users = Lists.newArrayList(userRepo.findAll());
            final List<User> usersWithSegments = new ArrayList<>();
            for (User user : users) {
                if (user != null) {
                    List<UserDataDirectory> userSegments = findSegments(user);
                    if (userSegments == null || userSegments.size() == 0){
//                        userSegments = getMockSegments();
                        continue;
                    }
                    user.setUserSegments(userSegments);
                    usersWithSegments.add(user);
                }
            }
            return usersWithSegments;
        } catch (Exception e){
            logger.error("An error occurred", e);
            return null;
        }
    }

//    private final String[] MOCK_SEGMENTS = {"Karaoke", "Sports", "Travel", "Politics", "Comedy", "Entertainment", "Movies", "Israel", "Animals", "RealEstate"};
//    private List<UserDataDirectory> getMockSegments(){
//        ArrayList<UserDataDirectory> result = new ArrayList<>();
//        int segmentCount = new Random(new Date().getTime()).nextInt(5);
//
//        final ArrayList<String> shuffledMockSegments = Lists.newArrayList(MOCK_SEGMENTS);
//        Collections.shuffle(shuffledMockSegments);
//
//        for (int i = 0; i < segmentCount; i++){
//            result.add(new UserDataDirectory(new Long(1), "", shuffledMockSegments.get(i),shuffledMockSegments.get(i)));
//        }
//
//        return result;
//    }

    private List<UserDataDirectory> findSegments(User user) {
        try {
            if (user != null) {
                final List<TaboolaIdentity> taboolaIdentities = user.getTaboolaIdentities();
                if (taboolaIdentities != null) {
                    final List<UserSegmentData> userSegmentDataList = taboolaIdentities.stream().map(t -> userSegmentRepo.findByTid(t.getTaboolaId())).flatMap(List::stream).collect(Collectors.toList());
                    final List<UserDataDirectory> userDataDirectoryList = userSegmentDataList.stream().map(u -> userDataDirectoryRepo.findBySegmentId(u.getSegment())).collect(Collectors.toList());

                    return userDataDirectoryList;
                }
            }
        } catch (Exception e){
            logger.error("An error occurred", e);
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
                user = userInput;
                newUsersCount++;
            }else{
                user.setPicture(userInput.getPicture());
            }
            try{
                userRepo.save(user);
            }
            catch (Throwable e){
                logger.error("An error occurred", e);
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
