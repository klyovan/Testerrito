package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/email/{email}")
    public User getCategoryById(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
