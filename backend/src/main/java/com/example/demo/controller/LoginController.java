package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")  // Маппинг для кастомной страницы логина
    public String login() {
        return "login.html"; // Указываем имя шаблона login.html
    }
}