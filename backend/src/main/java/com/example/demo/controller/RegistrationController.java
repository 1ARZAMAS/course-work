package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserRegistrationDto());
        return "registration"; // Возвращает имя шаблона
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid UserRegistrationDto userForm, 
                          BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration"; // Вернуть ту же страницу с ошибками
        }

        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration"; // Вернуть ту же страницу с ошибкой
        }

        return "redirect:/home_page"; // Перенаправить на страницу успеха
    }
}