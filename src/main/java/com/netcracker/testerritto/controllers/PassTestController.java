package com.netcracker.testerritto.controllers;


import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.services.TestService;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pass-test")
public class PassTestController {
    @Autowired
    TestService testService;

    @GetMapping("/test/{id}")
    public Test getTest(@PathVariable BigInteger id){
        try {
            return testService.getTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
