package com.warlock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

    @GetMapping("/register")
    public String signUp() {
        return "register";
    }

    @GetMapping("/messenger")
    public String chat() {
        return "messenger";
    }
}
