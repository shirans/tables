package com.taboola.tables.controllers;

import java.util.Arrays;
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
        final User user = userRepo.findByGmailId(gmail);

        final List<Appointment> appointments = user.getAppointments();
        appointments.sort((o1, o2) -> o1.getAppointmentDate().compareTo(o2.getAppointmentDate()));
        final Appointment nextAppointment = appointments.get(0);

        logger.info("appointment list is " + Arrays.toString(appointments.toArray()));
        logger.info("next appointment is " + nextAppointment.toString());


        return nextAppointment;
    }
}
