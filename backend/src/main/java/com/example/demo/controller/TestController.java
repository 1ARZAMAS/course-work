package com.example.demo.controller;

import com.example.demo.entity.TestEntity;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping
    public List<TestEntity> getAllTests() {
        return testService.getAllTests();
    }
}