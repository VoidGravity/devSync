package com.devsync.servlet;


import com.devsync.model.User;
import com.devsync.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;



@WebServlet("/user")
//@WebServlet("/")
//@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class mainServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.getUsers();

        req.setAttribute("users", users);
        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }
}
