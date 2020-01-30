package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GroupDAO;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

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
        checkUniquePhoneConstraint(user.getPhone());
        checkUniqueEmailConstraint(user.getEmail());

        checkParameter(user.getFirstName(), "firstName");
        checkParameter(user.getLastName(), "lastName");
        checkParameter(user.getPassword(), "password");

        return userDAO.createUser(user);
    }


    public User getUser(BigInteger id) throws ServiceException {
        checkUserId(id);
        User user = userDAO.getUser(id);
        user.setGroups(userDAO.getGroups(user.getId()));
        user.setCreatedGroups(userDAO.getCreatedGroups(user.getId()));
        return user;
    }

    @Deprecated
    public void deleteUser(BigInteger id) throws ServiceException {
        checkUserId(id);
        userDAO.deleteUser(id);
    }

    public void deleteUser(BigInteger id, String password) throws ServiceException {
        checkUserId(id);
        checkUserPassword(password);
        User userDel = userDAO.getUser(id);
        if (userDel.getPassword().equals(password)) {
            userDAO.deleteUser(id);
        } else {
            try {
                throw new IllegalArgumentException(
                    "Passwords don't match. Can't delete user with id = " + id);
            } catch (IllegalArgumentException ex) {
                serviceExceptionHandler.logAndThrowIllegalException(ex.getMessage());
            }
        }


    }

    private void checkUserPassword(String password) {
        if (password == null ) {
            try {
                throw new IllegalArgumentException(
                    "Password is null or empty string");
            } catch (IllegalArgumentException ex) {
                serviceExceptionHandler.logAndThrowIllegalException(ex.getMessage());
            }
        }
    }

    @Deprecated
    public void updateLastName(BigInteger id, String lastName) throws ServiceException {
        checkUserId(id);
        checkParameter(lastName, "lastName");
        userDAO.updateLastName(id, lastName);

    }

    @Deprecated
    public void updateFirstName(BigInteger id, String firstName) throws ServiceException {
        checkUserId(id);
        checkParameter(firstName, "firstName");
        userDAO.updateFirstName(id, firstName);
    }

    @Deprecated
    public void updateEmail(BigInteger id, String email) throws ServiceException {
        checkUserId(id);
        checkUniqueEmailConstraint(email);
        userDAO.updateEmail(id, email);
    }

    @Deprecated
    public void updatePassword(BigInteger id, String password) throws ServiceException {
        checkUserId(id);
        checkParameter(password, "password");
        userDAO.updatePassword(id, password);

    }

    @Deprecated
    public void updatePhone(BigInteger id, String phone) throws ServiceException {
        checkUserId(id);
        checkUniquePhoneConstraint(phone);
        userDAO.updatePhone(id, phone);

    }


    public User updateUser(User user) throws ServiceException {
        if (user == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object user can't be null");
        }
        checkUserId(user.getId());
        checkParameter(user.getLastName(), "lastName");
        checkParameter(user.getFirstName(), "firstName");
        checkParameter(user.getPhone(), "phone");

        User oldUser = userDAO.getUser(user.getId());

        if (!(oldUser.getPhone().equals(user.getPhone()))) {
            checkUniquePhoneConstraint(user.getPhone());
        }

        return userDAO.updateUser(user);
    }

    public List<Group> getGroups(BigInteger id) throws ServiceException {
        checkUserId(id);

        return fillGroupFields(userDAO.getGroups(id));


    }


    public List<Group> getCreatedGroups(BigInteger id) throws ServiceException {
        checkUserId(id);
        return fillGroupFields(userDAO.getCreatedGroups(id));
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

    public User getUserByEmail(String email) {
        checkParameter(email, "email");
        if (userDAO.isEmailExist(email)) {
           User user = userDAO.getUserByEmail(email);
            user.setGroups(userDAO.getGroups(user.getId()));
            user.setCreatedGroups(userDAO.getCreatedGroups(user.getId()));
            return user;
        }
        serviceExceptionHandler.logAndThrowIllegalException("This email: " + email + " don't exist");
        return null;
    }


    public User updateEmailAndPassword(List<User> userList) throws ServiceException {
        if (userList == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object user can't be null");
        }
        checkUserId(userList.get(0).getId());
        checkParameter(userList.get(0).getEmail(), "email");

        User oldUser = getUser(userList.get(0).getId());

        if (!(oldUser.getEmail().equals(userList.get(0).getEmail()))) {
            checkUniqueEmailConstraint(userList.get(0).getEmail());

            if (userList.get(0).getPassword() == null) {
                userList.get(0).setPassword(oldUser.getPassword());
                return userDAO.updateEmailAndPassword(userList.get(0));
            }
        }

        if (userList.get(0).getPassword() != null && userList.get(0).getPassword().equals(oldUser.getPassword())) {
            checkParameter(userList.get(1).getPassword(), "newPassword");

            userList.get(0).setPassword(userList.get(1).getPassword());
            return userDAO.updateEmailAndPassword(userList.get(0));
        }

        serviceExceptionHandler.logAndThrowIllegalException("passwords did not match");
        return null;
    }


    public User updatePassword(User user) throws ServiceException {
        if (user == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object user can't be null");
        }
        checkUserId(user.getId());
        checkParameter(user.getPassword(), "password");
        return userDAO.updatePassword(user);

    }

    public User getUserByPhone(String phone){
        checkParameter(phone, "phone");
        if (userDAO.isPhoneExist(phone)) {
            return userDAO.getUserByPhone(phone);
        }
       /// serviceExceptionHandler.logAndThrowIllegalException("This phone: " + phone + "don't exist");
        return null;
    }

    private List<Group> fillGroupFields(List<Group> groups) {
        List<Test> tests;
        List<User> users;

        for (Group group : groups) {
            tests = groupDAO.getAllTestsInGroup(group.getId());
            if (tests == null) {
                group.setTests(new LinkedList<>());
            }
            group.setTests(tests);

            users = groupDAO.getUsersInGroup(group.getId());
            if (users == null) {
                group.setUsers(new LinkedList<>());
            }
            group.setUsers(users);
        }
        return groups;

    }


    private void checkUserId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter userId can't be null");
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

    public Boolean checkUserConsistInGroup(BigInteger userId, BigInteger groupId) {
        if(userDAO.isUserConsistInGroup(userId, groupId)){
            serviceExceptionHandler.logAndThrowIllegalException("You already consist in that group!");
        } else{
            return false;
        }
                return false;
    }


}

