package com.devsync.dao;

public class UserDAOFactory {
    private static final UserDAO instance = new UserDAO();

    public static UserDAO getInstance() {
        return instance;
    }
}