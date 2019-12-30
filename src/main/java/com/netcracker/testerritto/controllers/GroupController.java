package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.*;
import com.netcracker.testerritto.services.GroupService;
import com.netcracker.testerritto.services.RemarkService;
import com.netcracker.testerritto.services.ResultService;
import com.netcracker.testerritto.services.UserService;
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

    @GetMapping("/{groupId}")
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

    //maybe we need method getAllRemarks()????
   /* @GetMapping("/remark")
    public Remark getAllRemarksInGroup(BigInteger groupId) throws IllegalArgumentException, ServiceException {
        return groupService.getAllRemarksInGroup(groupId);
    }*/

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
            return resultService.getResultsByUser(id);
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

    @PutMapping("/exitFromGroup/{id}")
    public void exitFromGroup(@RequestBody User user, @PathVariable BigInteger id ){
        try {
           userService.exitFromGroup(user.getId(), id );
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

}
