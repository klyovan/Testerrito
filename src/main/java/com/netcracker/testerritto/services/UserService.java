package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GroupDAO;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;


    public BigInteger createUser(User user) {
        if (user == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object user can't be null");
        }
        checkUniqueEmailConstraint(user.getEmail());
        checkUniquePhoneConstraint(user.getPhone());
        checkParameter(user.getFirstName(), "firstName");
        checkParameter(user.getLastName(), "lastName");
        checkParameter(user.getPassword(), "password");

        return userDAO.createUser(user);
    }

    public User getUser(BigInteger id) throws ServiceException {
        checkUserId(id);
        return userDAO.getUser(id);
    }

    public void deleteUser(BigInteger id) throws ServiceException {
        checkUserId(id);
        userDAO.deleteUser(id);
    }

    public void updateLastName(BigInteger id, String lastName) throws ServiceException {
        checkUserId(id);
        checkParameter(lastName, "LastName");
        userDAO.updateLastName(id, lastName);

    }

    public void updateFirstName(BigInteger id, String firstName) throws ServiceException {
        checkUserId(id);
        checkParameter(firstName, "firstName");
        userDAO.updateFirstName(id, firstName);
    }

    public void updateEmail(BigInteger id, String email) throws ServiceException {
        checkUserId(id);
        checkUniqueEmailConstraint(email);
        userDAO.updateEmail(id, email);
    }

    public void updatePassword(BigInteger id, String password) throws ServiceException {
        checkUserId(id);
        checkParameter(password, "password");
        userDAO.updatePassword(id, password);

    }

    public void updatePhone(BigInteger id, String phone) throws ServiceException {
        checkUserId(id);
        checkUniquePhoneConstraint(phone);
        userDAO.updatePhone(id, phone);

    }

    public List<Group> getGroups(BigInteger id) throws ServiceException {
        checkUserId(id);
        return userDAO.getGroups(id);
    }

    public List<Group> getCreatedGroups(BigInteger id) throws ServiceException {
        checkUserId(id);
        return userDAO.getCreatedGroups(id);
    }

    public void deleteCreatedGroup(BigInteger userId, BigInteger createdGroupId) throws ServiceException {

        checkUserId(userId);
        checkGroupId(createdGroupId);

        List<Group> groupList = getCreatedGroups(userId);
        boolean match = false;
        for (Group group : groupList) {
            if (group.getId().equals(createdGroupId)) {
                match = true;
            }
        }
        if (match == false) {
            serviceExceptionHandler.logAndThrowIllegalException("userId:" + userId +
                " has not createdGroup with groupId: " + createdGroupId);
        }
        groupDAO.deleteGroup(createdGroupId);

    }

    public void enterInGroup(BigInteger userId, BigInteger groupId) throws ServiceException {
        checkUserId(userId);
        checkGroupId(groupId);

        try {
            userDAO.enterInGroup(userId, groupId);
        } catch (DataAccessException ex) {
            serviceExceptionHandler.logAndThrowServiceException("userId:" + userId +
                " Is already consist in group with groupId: " + groupId, ex);
        }

    }

    public void exitFromGroup(BigInteger userId, BigInteger groupId) throws ServiceException {
        checkUserId(userId);
        checkGroupId(groupId);

        List<Group> groupList = getGroups(userId);
        boolean match = false;
        for (Group group : groupList) {
            if (groupId.equals(group.getId())) {
                match = true;
                break;
            }
        }
        if (match == false) {
            serviceExceptionHandler.logAndThrowIllegalException("userId:" + userId +
                " does not consist in groupId: " + groupId);
        }

        userDAO.exitFromGroup(userId, groupId);

    }


    private void checkUserId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter user_id can't be null");
        }
        try {
            userDAO.getUser(id);
        } catch (DataAccessException ex) {
            serviceExceptionHandler
                .logAndThrowServiceException("Getting user by id:" + id + " was fail", ex);
        }
    }

    private void checkGroupId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter group_id can't be null");
        }
        try {
            groupDAO.getGroupById(id);
        } catch (DataAccessException ex) {
            serviceExceptionHandler
                .logAndThrowServiceException("Getting group by id:" + id + " was fail", ex);
        }
    }


    private void checkParameter(String param, String paramName) {
        String message = "Parameter(" + paramName + ")";

        if (param == null) {
            serviceExceptionHandler.logAndThrowIllegalException(message + " can't be null");
        } else if ("".equals(param)) {
            serviceExceptionHandler.logAndThrowIllegalException(message + " can't be empty");
        }
    }

    private void checkUniqueEmailConstraint(String param) {
        checkParameter(param, "email");

        if (userDAO.isEmailExist(param)) {

            try {
                throw new IllegalArgumentException(
                    "Email: " + param + " already exists in database");
            } catch (IllegalArgumentException ex) {
                serviceExceptionHandler.logAndThrowIllegalException(ex.getMessage());
            }
        }

    }

    private void checkUniquePhoneConstraint(String param) {
        checkParameter(param, "phone");

        if (userDAO.isPhoneExist(param)) {
            try {
                throw new IllegalArgumentException(
                    "Phone: " + param + " already exists in database");
            } catch (IllegalArgumentException ex) {
                serviceExceptionHandler.logAndThrowIllegalException(ex.getMessage());
            }
        }

    }

}

