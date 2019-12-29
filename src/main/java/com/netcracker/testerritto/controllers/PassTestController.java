package com.netcracker.testerritto.controllers;


import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.services.ReplyService;
import com.netcracker.testerritto.services.ResultService;
import com.netcracker.testerritto.services.TestService;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pass-test")
public class PassTestController {

    @Autowired
    TestService testService;
    @Autowired
    ResultService resultService;
    @Autowired
    ReplyService replyService;

    private BigInteger createdResultId = BigInteger.valueOf(120);

    @PutMapping("/test/{id}")
    public Test getTest(@PathVariable BigInteger id, @RequestBody Result result) {
        try {
            createdResultId = resultService.createResult(result);

            return testService.getTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

//    @GetMapping("/test/{id}")
//    public Test getTestDetails(@PathVariable BigInteger id){
//        try {
//            return testService.getTest(id);
//        } catch (IllegalArgumentException | ServiceException e) {
//            throw new ApiRequestException(e.getMessage(), e);
//        }
//
//    }

    @GetMapping("/reply")
    public Result getReplies() {
        try {
            return resultService.getResult(createdResultId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/addReply")
    public BigInteger createReply(@RequestBody Reply reply) {
        try {
            return replyService.createReply(reply);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
