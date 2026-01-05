package com.example.stand_up_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "login"; // Va cauta login.html in templates
    }

    @GetMapping("/register")
    public String paginaRegister() {
        return "register"; // Va cauta register.html in templates
    }

    @GetMapping("/dashboard")
    public String paginaDashboard() {
        return "dashboard";
    }

    @GetMapping("/index")
    public String paginaHome() {
        return "index"; // Va căuta index.html în templates
    }
}