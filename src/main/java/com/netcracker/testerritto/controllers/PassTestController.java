package com.netcracker.testerritto.controllers;


import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.services.QuestionService;
import com.netcracker.testerritto.services.ReplyService;
import com.netcracker.testerritto.services.ResultService;
import com.netcracker.testerritto.services.TestService;

import java.math.BigInteger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Autowired
    QuestionService questionService;

    private List<BigInteger> createdResults = new ArrayList<>();

    @GetMapping("{userId}/test/{id}")
    public Test getTest(@PathVariable BigInteger userId, @PathVariable BigInteger id) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-DD");

        try {
           List<Result> results = resultService.getResultsByUser(userId);

            String localdate = fmt.format(new Date());

//           results.forEach(result ->
//               {
//                   String resultDate = fmt.format(result.getDate());
//                   if (resultDate.equals(localdate) && result.getTestId().equals(id) && result.getUserId().equals(userId)) {
//                      throw  new ApiRequestException("Results Already exists!");
//                   }
//               }
//               );

            createdResults = resultService.createResultsByCategories(id, userId);
            return testService.getTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/reply")
    public List<Result> getReplies() {
        try {
            List<Result> results = new ArrayList<>();

            for (BigInteger createdResult : createdResults) {
                results.add(resultService.getResult(createdResult));
            }

            return results;
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/addreply")
    public BigInteger createReply(@RequestBody List<Answer> answers ) {
        try {
            Reply reply = new Reply();
            BigInteger questionId = answers.get(0).getQuestionId();
            Question question = questionService.getQuestionById(questionId);
            BigInteger answer2category = question.getCategoryId();

            for (BigInteger createdResult : createdResults) {
                if (resultService.getResult(createdResult).getCategoryId().equals(answer2category)) {
                    reply.setResultId(createdResult);
                }
            }
            reply.setReplyList(answers);
            System.out.println(reply + "LOlec)");
            return replyService.createReply(reply);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
