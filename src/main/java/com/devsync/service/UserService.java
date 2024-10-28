package com.devsync.service;

import com.devsync.dao.UserDAO;
import com.devsync.model.User;
import com.devsync.util.JPAutil;

import java.util.Date;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // For testing purposes
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if username already exists
        User existingUser = userDAO.findUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        boolean created = userDAO.create(user);
        return created ? user : null;
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userDAO.findUserByUsername(username);
    }

    public User getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userDAO.getUserById(id);
    }

    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        userDAO.updateUser(user);
        JPAutil.clearCache();

    }

    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        userDAO.deleteUser(user);
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = userDAO.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public User findUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userDAO.getUserById(user.getId());
    }
    public void processChangeRequests() {
        if (userDAO == null) {
            throw new IllegalStateException("UserDAO is not initialized");
        }

        List<User> usersWithPendingRequests = userDAO.getUsersWithPendingChangeRequests();
        if (usersWithPendingRequests == null || usersWithPendingRequests.isEmpty()) {
            return;
        }

        Date twelveHoursAgo = new Date(System.currentTimeMillis() - 12 * 60 * 60 * 1000);

        for (User user : usersWithPendingRequests) {
            if (user.getLastTokenReset() != null && user.getLastTokenReset().before(twelveHoursAgo)) {
                user.doubleModificationTokens();
                userDAO.updateUser(user);
            }
        }
    }
    public String getUserRole(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        return userDAO.getUserRole(user);
    }
}