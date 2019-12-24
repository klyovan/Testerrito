package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
}
