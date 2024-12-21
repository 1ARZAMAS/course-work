package com.example.demo.service;

import com.example.demo.entity.TestEntity;
import com.example.demo.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public List<TestEntity> getAllTests() {
        return testRepository.findAll();
    }
}