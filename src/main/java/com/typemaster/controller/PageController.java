package com.typemaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller mapping view requests to static HTML resources in Spring Boot.
 */
@Controller
public class PageController {

    @GetMapping({"/home", "/home page", "/home-page"})
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

    @GetMapping({"/about", "/about-us"})
    public String aboutAlias() {
        return "forward:/about.html";
    }

    @GetMapping({"/contact", "/contact-us"})
    public String contactAlias() {
        return "forward:/contact.html";
    }

    @GetMapping({"/privacy", "/privacy-policy"})
    public String privacyAlias() {
        return "forward:/privacy.html";
    }

    @GetMapping({"/terms", "/terms-conditions"})
    public String termsAlias() {
        return "forward:/terms.html";
    }

    @GetMapping("/disclaimer")
    public String disclaimerAlias() {
        return "forward:/disclaimer.html";
    }

    @GetMapping("/blog")
    public String blogAlias() {
        return "forward:/blog.html";
    }
}
