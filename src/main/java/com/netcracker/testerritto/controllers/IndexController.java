package com.netcracker.testerritto.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class IndexController {
    @CrossOrigin("https://testingtos.herokuapp.com/")
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
