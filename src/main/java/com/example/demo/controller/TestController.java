package com.example.demo.controller;

import com.example.demo.domain.Config;
import com.example.demo.domain.User;
import com.example.demo.service.SparkTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TC20021 on 2018/11/6.
 */
@RestController
public class TestController {

    @Value("${blog.name}")
    String name;
    @Value("${blog.age}")
    int age;
    @Autowired
    Config config;

    /*@Autowired
    private SparkTestService sparkTestService;
    @RequestMapping("/hi")
    public String index(){

        return sparkTestService.sparkDemo();
    }*/
}
