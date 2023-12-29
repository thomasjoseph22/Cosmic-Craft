package com.example.admin.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginForm(HttpSession session) {
        Object attribute = session.getAttribute("userLoggedIn");
        if (attribute != null) {
            return "redirect:/";
        }
        return "login";
    }



}


