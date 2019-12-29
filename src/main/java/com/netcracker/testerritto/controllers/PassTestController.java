package com.netcracker.testerritto.controllers;


import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.services.ReplyService;
import com.netcracker.testerritto.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("pass-test")
public class PassTestController {
    @Autowired
    TestService testService;
    @Autowired
    ReplyService replyService;

    @GetMapping("/test/{id}")
    public Test getTest(@PathVariable BigInteger id){
        try {
            return testService.getTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/addReply")
    public BigInteger createReply(@RequestBody Reply reply){
        try {
            return replyService.createReply(reply);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
