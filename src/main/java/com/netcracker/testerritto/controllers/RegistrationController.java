package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;


@RestController
@RequestMapping("registration")

public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    @PostMapping()
    public BigInteger creteUser(@RequestBody User user) {

        try {
            return userService.createUser(user);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {

        if (userDAO.isEmailExist(email)) {
            return userDAO.getUserByEmail(email);
        }
        return null;
    }

    @GetMapping("/phone/{phone}")
    public User getUserByPhone(@PathVariable String phone) {

        return userService.getUserByPhone(phone);

    }

}
