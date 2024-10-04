package com.devsync.service;

import com.devsync.dao.UserDAO;
import com.devsync.model.User;

import java.util.List;

public class UserService {


    public void createUser(User user){
        UserDAO.create(user);
    }
    public List<User> getUsers(){
        List<User> users = UserDAO.getUsers();
        return users;
    }
    public User findUser(User user){
       return UserDAO.findUser(user);

    }
    public void deleteUser(User user){
        UserDAO.deleteUser(user);
    }
    public void updateUser(User user){

    UserDAO.updateUser(user);
    }
}
