package com.taboola.tables.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.db.Appointment;
import com.taboola.tables.db.AppointmentRepo;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    @RequestMapping("/get-appointment")
    public Appointment getAppointment(@RequestParam(value="GmailId") String gmail) {
        if (gmail == null || gmail.isEmpty()) {
            logger.error("Invalid gmail "+gmail);
            return null;
        }

        try{
            final User user = userRepo.findByGmailId(gmail);

            final List<Appointment> appointments = user.getAppointments();
            appointments.sort((o1, o2) -> o1.getAppointmentDate().compareTo(o2.getAppointmentDate()));
            final Appointment nextAppointment = appointments.get(0);

            logger.info("appointment list is " + Arrays.toString(appointments.toArray()));
            logger.info("next appointment is " + nextAppointment.toString());

            return nextAppointment;
        }
        catch (Throwable e){
            e.printStackTrace();
            return getFirstAppointment();
        }
    }


    private Appointment getMockAppointment(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Person 1", "1", "person1@gmail.com"));
        users.add(new User("Person 2", "2", "person2@gmail.com"));
        users.add(new User("Person 3", "3", "person3@gmail.com"));
        users.add(new User("Person 4", "4", "person4@gmail.com"));

        Appointment mock = new Appointment(1, "Here", LocalDateTime.now(), users);
        return mock;
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
