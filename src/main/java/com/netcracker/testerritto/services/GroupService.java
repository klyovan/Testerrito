package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GroupDAO;
import com.netcracker.testerritto.dao.RemarkDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private RemarkDAO remarkDAO;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    public Group getGroupById(BigInteger groupId) throws ServiceException {
        checkIdNotNull(groupId);
        try {
            Group group = groupDAO.getGroupById(groupId);
            group.setTests(groupDAO.getAllTestsInGroup(groupId));
            group.setUsers(new ArrayList<>());
            return group;
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed GetGroupById().", exception);
            return null;
        }
    }

    public BigInteger createGroup(Group group) throws ServiceException {
        checkIdNotNull(group.getCreatorUserId());
        checkStringNotNull(group.getLink());
        checkStringNotNull(group.getName());
        checkUniqueName(group.getCreatorUserId(), group.getName());
       try {
            return groupDAO.createGroup(group);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed CreateGroup().", exception);
            return null;
        }
    }

    public Group updateGroup(Group group) throws ServiceException {
        checkIdNotNull(group.getId());
        checkStringNotNull(group.getName());
        checkUniqueName(group.getCreatorUserId(), group.getName());
        try {
            return groupDAO.updateGroup(group);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed UpdateGroup().", exception);
            return null;
        }
    }

    public void deleteGroup(BigInteger groupId) throws ServiceException {
        checkIdNotNull(groupId);
        try {
            groupDAO.deleteGroup(groupId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed DeleteGroup().", exception);
        }
    }

    public List<User> getUsersInGroup(BigInteger groupId) throws ServiceException {
        checkIdNotNull(groupId);
        try {
            return groupDAO.getUsersInGroup(groupId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed GetUsersInGroup().", exception);
            return new ArrayList<>();
        }
    }

    public List<Test> getAllTestsInGroup(BigInteger groupId) throws ServiceException {
        checkIdNotNull(groupId);
        try {
            return groupDAO.getAllTestsInGroup(groupId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed GetAllTestsInGroup().", exception);
            return new ArrayList<>();
        }
    }

    public List<Remark> getAllRemarksInGroup(BigInteger groupId) throws ServiceException {
        checkIdNotNull(groupId);
        try {
            return groupDAO.getAllRemarksInGroup(groupId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed GetAllRemarksInGroup().", exception);
            return new ArrayList<>();
        }
    }

    private void checkIdNotNull(BigInteger id) {
        if (id == null)
            serviceExceptionHandler.logAndThrowIllegalException("Parameter ID can not be NULL");
    }

    private void checkStringNotNull(String string) {
        if ("".equals(string) || string == null)
            serviceExceptionHandler.logAndThrowIllegalException("Parameter Text can not be NULL");
    }

    private void checkUniqueName(BigInteger userId, String groupName) {
        if(groupDAO.isGroupNameExist(userId, groupName))
            serviceExceptionHandler.logAndThrowIllegalException("Group with name " + groupName + " already exist");
    }
}
