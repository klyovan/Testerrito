package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("registration")
//@CrossOrigin(origins = "*")//(origins = "http://localhost:4200/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @CrossOrigin("https://testingtos.herokuapp.com/")
    @PostMapping()
    public BigInteger creteUser(@RequestBody User user) {

        try {
            return userService.createUser(user);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @CrossOrigin("https://testingtos.herokuapp.com/")
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (IllegalArgumentException | ServiceException e) {
           // throw new ApiRequestException(e.getMessage(), e);
            return null;
        }
    }

    @CrossOrigin("https://testingtos.herokuapp.com/")
    @GetMapping("/phone/{phone}")
    public User getUserByPhone(@PathVariable String phone){
//        try {
            return userService.getUserByPhone(phone);
//        } catch (IllegalArgumentException | ServiceException e) {
//            throw new ApiRequestException(e.getMessage(), e);
//        }
    }

}
