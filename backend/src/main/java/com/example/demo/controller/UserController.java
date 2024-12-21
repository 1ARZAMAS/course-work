package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserEntity user) {
        userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserEntity user) {
        userService.validateUser(user.getUsername(), user.getPassword());
        return "Login successful!";
    }
}