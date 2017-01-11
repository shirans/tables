package com.taboola.tables.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Controller
public class DefaultPageController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultPage() {
        return "redirect:/home.html";
    }
}
