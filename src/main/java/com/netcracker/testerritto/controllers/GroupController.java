package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.*;
import com.netcracker.testerritto.services.GroupService;
import com.netcracker.testerritto.services.RemarkService;
import com.netcracker.testerritto.services.ResultService;
import com.netcracker.testerritto.services.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public BigInteger createGroup(@RequestBody Group group) {
        try {
            return groupService.createGroup(group);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/{groupId}")
    public Group getGroupBuId(@PathVariable BigInteger groupId) throws IllegalArgumentException, ServiceException {
        try {
            return groupService.getGroupById(groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroupById(@PathVariable BigInteger groupId) throws IllegalArgumentException, ServiceException {
        try {
            groupService.deleteGroup(groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping
    public Group updateGroup(@RequestBody Group group) {
        try {
            return groupService.updateGroup(group);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/remark/{remarkId}")
    public Remark getRemarkById(@PathVariable BigInteger remarkId) throws IllegalArgumentException, ServiceException {
        try {
            return remarkService.getRemarkById(remarkId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/remark/{remarkId}")
    public void deleteRemark(@PathVariable BigInteger remarkId) throws IllegalArgumentException, ServiceException {
        try {
            remarkService.deleteRemark(remarkId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/{groupId}/alltests")
    public List<Test> getAllTestsInGroup(@PathVariable BigInteger groupId) throws IllegalArgumentException, ServiceException {
        try {
            return groupService.getAllTestsInGroup(groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/{groupId}/users")
    public List<User> getUsersInGroup(@PathVariable BigInteger groupId) throws IllegalArgumentException, ServiceException {
        try {
            return groupService.getUsersInGroup(groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/{groupId}/remarks")
    public List<Remark> getAllRemarksInGroup(@PathVariable BigInteger groupId) throws IllegalArgumentException, ServiceException {
        try {
            return groupService.getAllRemarksInGroup(groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/result/{id}")
    public Result getResultPassedTest(@PathVariable BigInteger id) {
        try {
            return resultService.getResult(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }

    }

    @GetMapping("/result/user/{id}")
    public List<Result> getResultsPassedTestByUser(@PathVariable BigInteger id) {
        try {
            Map<BigInteger, Integer> scores = new HashMap<>();

            List<Result> results = resultService.getResultsByUser(id);

            results.forEach(result -> {
                scores.put(result.getCategoryId(), 0);
            });

            for (Result result : results) {

                for (Entry<Question, Reply> entry : result.getReplies().entrySet()) {
                    Question question = entry.getKey();
                    Reply reply = entry.getValue();
                    reply.getReplyList().forEach(answer -> {
                        scores.forEach((categoryId, score) -> {
                            if (categoryId.equals(result.getCategoryId())) {
                                int newvalue = score + answer.getScore();
                                scores.put(categoryId, newvalue);
                            }
                        });
                    });
                }
            }

            results.forEach(result -> {
                scores.forEach((bigInteger, integer) -> {
                    if (bigInteger.equals(result.getCategoryId())) {
                        result.setScore(integer);
                    }
                });
            });

            return results;
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }


    @GetMapping("/result/test/{id}")
    public List<Result> getResultsPassedTestByTest(@PathVariable BigInteger id) {
        try {
            return resultService.getResultsByTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{groupId}/exitfromgroup/{userId}")
    public void exitFromGroup(@PathVariable BigInteger groupId, @PathVariable BigInteger userId ){
        try {
           userService.exitFromGroup(userId, groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

   /* @GetMapping("/{id}/results")
    public void showResultsForTest(@PathVariable BigInteger id){

    }*/

    @PutMapping("/remarkviewed/{remarkId}")
    public void updateRemarkViewStatus(@PathVariable BigInteger remarkId){
        try {
            remarkService.updateRemarkViewStatus(remarkId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
