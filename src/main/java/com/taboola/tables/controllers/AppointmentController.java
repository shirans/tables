package com.taboola.tables.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.json.Appointment;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController
public class AppointmentController {


    @RequestMapping("/get-appointment")
    public Appointment getAppointment(@RequestParam(value="userName", defaultValue="") String name) {
        final List<String> participates = new ArrayList<>();
        participates.add("Boaz");
        participates.add("Eyal");
        participates.add("Shiran");
        return new Appointment(1, name, participates, "At Taboola Kitchen!!!!",System.currentTimeMillis());
    }
}
