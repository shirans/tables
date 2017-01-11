package com.taboola.tables.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taboola.tables.json.Appointment;

/**
 * Created by boaz.y on 11/01/2017.
 */
@RestController("TablesController")
public class TablesController {


    @RequestMapping("/getappointment")
    public Appointment greeting(@RequestParam(value="userName", defaultValue="Boaz") String name) {
        return new Appointment(1, name,"At Taboola Kitchen!!!!",System.currentTimeMillis());
    }
}
