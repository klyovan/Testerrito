package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/userGroups/{id}")
    public List<Group> getGroups(@PathVariable BigInteger id) {

        try {
            return userService.getGroups(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/userCreatedGroups/{id}")
    public List<Group> getCreatedGroups(@PathVariable BigInteger id){
        try {
            return userService.getCreatedGroups(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable BigInteger id){
        try{
             userService.deleteUser(id);
        }catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("deleteGroup")
    public void deleteCreatedGroup(@RequestBody Group group){
        try{
            userService.deleteCreatedGroup(group.getCreatorUserId(), group.getId());
        }catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }





//    @PutMapping("/updateLastName/{id}")
//    public void updateLastName(@PathVariable BigInteger id, @RequestBody String lastName){
//        try {
//             userService.updateLastName( id, lastName);      //return
//        } catch (IllegalArgumentException | ServiceException e) {
//            throw new ApiRequestException(e.getMessage(), e);
//        }
//    }






}
