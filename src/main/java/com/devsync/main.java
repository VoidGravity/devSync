package com.devsync;

import com.devsync.dao.UserDAO;
import com.devsync.model.User;

public class main {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user = UserDAO.findUser(user);
        System.out.println(user.getUsername());
    }

}
