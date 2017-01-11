package com.taboola.tables.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.db.Appointment;
import com.taboola.tables.db.AppointmentRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class AppointmentHistory {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @RequestMapping("/get-appointment-history")
    public Iterable<Appointment> getAppointmentHistory() {
        return appointmentRepo.findAll();
    }
}
