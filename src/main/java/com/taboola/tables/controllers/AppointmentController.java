package com.taboola.tables.controllers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.db.Appointment;
import com.taboola.tables.db.AppointmentRepo;
import com.taboola.tables.db.TaboolaIdentity;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserDataDirectory;
import com.taboola.tables.db.UserDataDirectoryRepo;
import com.taboola.tables.db.UserRepo;
import com.taboola.tables.db.UserSegmentData;
import com.taboola.tables.db.UserSegmentRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    private final String[] MOCK_SEGMENTS = {"Karaoke", "Sports", "Travel", "Politics", "Comedy", "Entertainment", "Movies", "Israel", "Animals", "RealEstate"};

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private UserSegmentRepo userSegmentRepo;

    @Autowired
    private UserDataDirectoryRepo userDataDirectoryRepo;

    @RequestMapping("/get-appointment")
    public Appointment getAppointment(@RequestParam(value="GmailId") String gmail) {
        if (gmail == null || gmail.isEmpty()) {
            logger.error("Invalid gmail "+gmail);
            return null;
        }

        try{
            final User user = userRepo.findByGmailId(gmail);

            if (user == null){
                return getFirstAppointment();
            }

            final List<Appointment> appointments = user.getAppointments();
            appointments.sort((o1, o2) -> o1.getAppointmentDate().compareTo(o2.getAppointmentDate()));
            final Appointment nextAppointment = appointments.get(0);

            logger.info("appointment list is " + Arrays.toString(appointments.toArray()));
            logger.info("next appointment is " + nextAppointment.toString());

            addSegments(nextAppointment);

            return nextAppointment;
        }
        catch (Throwable e){
            e.printStackTrace();
            return getFirstAppointment();
        }
    }

    private void addSegments(Appointment appointment){
        final List<User> users = appointment.getUsers();
        for (User user : users) {
            if (user != null && (user.getUserSegments() == null || user.getUserSegments().isEmpty())) {
                List<UserDataDirectory> userSegments = findSegments(user);
                if (userSegments == null || userSegments.size() == 0){
                    userSegments = getMockSegments();
                }

                user.setUserSegments(getMockSegments());
            }
        }
    }

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
            return null;
        }
        return null;
    }

    private Appointment getRandomAppointment(){
        return getFirstAppointment();
    }

    private List<UserDataDirectory> getMockSegments(){
        ArrayList<UserDataDirectory> result = new ArrayList<>();
        int segmentCount = new Random(new Date().getTime()).nextInt(5);

        final ArrayList<String> shuffledMockSegments = Lists.newArrayList(MOCK_SEGMENTS);
        Collections.shuffle(shuffledMockSegments);

        for (int i = 0; i < segmentCount; i++){
            result.add(new UserDataDirectory(new Long(1), "", shuffledMockSegments.get(i),shuffledMockSegments.get(i)));
        }

        return result;
    }

    private Appointment getMockAppointment(){
        ArrayList<User> users = new ArrayList<>();
        users.add(createMockUser(1));
        users.add(createMockUser(2));
        users.add(createMockUser(3));
        users.add(createMockUser(4));

        Appointment mock = new Appointment(1, "Here", LocalDateTime.now(), users);
        return mock;
    }

    private User createMockUser(Integer number){
        User newUser = new User("Person " + number, number.toString(), "person" + number + "@gmail.com");
        newUser.setScore((double) (number + 10));
        return newUser;
    }

    private Appointment getFirstAppointment(){
        try{
            Iterator<Appointment> cursor = appointmentRepo.findAll().iterator();
            return cursor.next();
        }
        catch (Throwable e){
            e.printStackTrace();
            return getMockAppointment();
        }
    }
}
