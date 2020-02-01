package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable BigInteger id) {
        try {
            return userService.getUser(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {

        try {
            return userService.updateUser(user);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }


    @PutMapping("/credentials")
    public User updateEmailAndPassword(@RequestBody ArrayList<User> userList) {

        try {

            return userService.updateEmailAndPassword(userList);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/enter/{groupId}/{userId}")
    public void enterInGroup(@PathVariable BigInteger userId, @PathVariable BigInteger groupId ){
        try {
             userService.enterInGroup(userId,groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }

    }

    @GetMapping("/check/consist/{groupId}/{userId}")
    public Boolean checkUserConsistInGroup(@PathVariable BigInteger userId, @PathVariable BigInteger groupId ){
        try {
            return userService.checkUserConsistInGroup(userId,groupId);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }

    }


}
