package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home_page")
    public String homePage() {
        return "home_page"; // Возвращает шаблон home_page.html
    }
}