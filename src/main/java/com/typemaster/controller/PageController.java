package com.typemaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller mapping view requests to static HTML resources in Spring Boot.
 */
@Controller
public class PageController {

    @GetMapping({"/home", "/home.html", "/home page", "/home-page"})
    public String homeAlias() {
        return "forward:/index.html";
    }

    @GetMapping("/typing")
    public String typingAlias() {
        return "forward:/typing.html";
    }

    @GetMapping("/result")
    public String resultAlias() {
        return "forward:/result.html";
    }

    @GetMapping({"/auth", "/login", "/signup"})
    public String authAlias() {
        return "forward:/auth.html";
    }
}
