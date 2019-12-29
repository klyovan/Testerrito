package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
/////////хер пойми
    @GetMapping("/email/{email}")
    public User getCategoryById(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
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


}
