package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.GroupService;
import com.netcracker.testerritto.services.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

}
