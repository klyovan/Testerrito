package com.netcracker.testerritto.controllers;


import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.properties.ListsAttr;
import com.netcracker.testerritto.properties.Status;
import com.netcracker.testerritto.services.QuestionService;
import com.netcracker.testerritto.services.RemarkService;
import com.netcracker.testerritto.services.ReplyService;
import com.netcracker.testerritto.services.ResultService;
import com.netcracker.testerritto.services.TestService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private RemarkService remarkService;

    private List<BigInteger> createdResults = new ArrayList<>();
    private List<Reply> createdReplies = new ArrayList<>();
    private int isUpdate = 0;
    private BigInteger replyId;
    private List<Answer> oldReplyList = new ArrayList<>();

    private List<Answer> newReplyList = new ArrayList<>();

    @GetMapping("{userId}/test/{id}")
    public Test getTest(@PathVariable BigInteger userId, @PathVariable BigInteger id) {

        try {

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

            for (Result result : results) {
                result.setStatus(Status.PASSED);
                resultService.updateResult(result);
            }
            createdReplies = new ArrayList<>();
            createdResults = new ArrayList<>();
            return results;
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/reply")
    public BigInteger createReply(@RequestBody Reply reply) {
        try {
            int oldReplyIndex = 0;
            if (reply.getReplyList().size() > 0) {
                oldReplyList = new ArrayList<>();
                newReplyList = new ArrayList<>();
                for (int i = 0; i < createdReplies.size(); i++) {
                    if (createdReplies.get(i).getReplyList().get(0).getQuestionId().equals(reply.getReplyList().get(0).getQuestionId())) {
                        isUpdate++;
                        reply.setId(createdReplies.get(i).getId());
                        reply.setResultId(createdReplies.get(i).getResultId());
                        oldReplyList.add(createdReplies.get(i).getReplyList().get(0));
                        oldReplyIndex = i;
                    }
                }

                if (isUpdate == 0) {
                    BigInteger questionId = reply.getReplyList().get(0).getQuestionId();
                    Question question = questionService.getQuestionById(questionId);
                    BigInteger answer2category = question.getCategoryId();

                    for (BigInteger createdResult : createdResults) {
                        if (resultService.getResult(createdResult).getCategoryId().equals(answer2category)) {
                            reply.setResultId(createdResult);
                        }
                    }

                    replyId = replyService.createReply(reply);
                    reply.setId(replyId);
                    createdReplies.add(reply);

                    isUpdate = 0;
                    return replyId;
                } else if (isUpdate > 0) {

                    BigInteger questionId = reply.getReplyList().get(0).getQuestionId();
                    ListsAttr answerType = questionService.getQuestionById(questionId).getTypeQuestion();

                    if (answerType == ListsAttr.ONE_ANSWER || answerType == ListsAttr.OPEN) {
                        newReplyList = reply.getReplyList();
                        reply.setReplyList(oldReplyList);

                        replyService.updateReply(reply, newReplyList.get(0));

                        reply.setReplyList(newReplyList);
                        createdReplies.set(oldReplyIndex, reply);

                        isUpdate = 0;

                    } else if (answerType == ListsAttr.MULTIPLE_ANSWER) {

                        replyService.deleteReply(reply.getId());
                        createdReplies.remove(reply);

                        replyId = replyService.createReply(reply);
                        reply.setId(replyId);

                        createdReplies.add(reply);

                        isUpdate = 0;


                        return replyId;
                    }
                }
            } else {
                return null;
            }
            return null;
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/remark")
    public BigInteger createRemark(@RequestBody Remark remark) throws IllegalArgumentException, ServiceException {
        try {
            return remarkService.createRemark(remark);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("{userId}/test/finish/{testId}")
    public Test finishTest(@PathVariable BigInteger userId, @PathVariable BigInteger testId) {
        try {
            System.out.println("ZAHALO");
            List<Result> results = resultService.getResultsByTest(testId);
            createdResults = new ArrayList<>();

            for (Result result : results) {
                if (result.getUserId().equals(userId) && result.getStatus() == Status.NOT_PASSED) {
                    createdResults.add(result.getId());
                }
            }

            Test test = testService.getTest(testId);

            for (Result result : results) {
                if (result.getUserId().equals(userId) && result.getStatus() == Status.NOT_PASSED) {
                    result.getReplies().keySet().iterator().forEachRemaining(test.getQuestions()::remove);
                }
            }

            return test;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }


    }


}
