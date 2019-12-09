package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GroupDAO;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupDAO groupDAO;

    private static Logger log=Logger.getLogger(RemarkService.class.getName());

    public Group getGroupById(BigInteger groupId){
        checkNullId(groupId);
        return groupDAO.getGroupById(groupId);
    }

    public BigInteger createGroup(BigInteger userId, String link, String name){
        checkNullId(userId);
        checkNullString(link);
        checkNullString(name);
        return groupDAO.createGroup(userId, link, name);
    }

    public void updateGroup(BigInteger groupId, String newName){
        checkNullId(groupId);
        checkNullString(newName);
        groupDAO.updateGroup(groupId, newName);
    }

    public void deleteGroup(BigInteger groupId){
        checkNullId(groupId);
        groupDAO.deleteGroup(groupId);
    }

    public List<User> getUsersInGroup(BigInteger groupId){
        checkNullId(groupId);
        return groupDAO.getUsersInGroup(groupId);
    }

    public List<Test> getAllTestsInGroup(BigInteger groupId){
        checkNullId(groupId);
        return groupDAO.getAllTestsInGroup(groupId);
    }

    private void checkNullId(BigInteger id){
        if(id == null){
            IllegalArgumentException exception = new IllegalArgumentException("ID can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), id);
            throw exception;
        }
    }

    private void checkNullString(String text){
        if("".equals(text)){
            IllegalArgumentException exception = new IllegalArgumentException("Text Field can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), text);
            throw exception;
        }
    }
}
