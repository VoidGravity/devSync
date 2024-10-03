package com.devsync.servlet;

import com.devsync.model.ManagerRole;
import com.devsync.model.User;
import com.devsync.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/user/*")
public class TestServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
//        String path = req.getServletPath();

        switch (path) {
            case "/create":
                req.getRequestDispatcher("/WEB-INF/jsp/createUser.jsp").forward(req, resp);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String path = req.getPathInfo();
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String lname = req.getParameter("lname");
        String fname = req.getParameter("fname");
        String password = req.getParameter("password");

        ManagerRole managerRole = ManagerRole.MANAGER;


        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(user.getFirstName());
        user.setLastName(lname);
        user.setEmail(email);
        user.setManagerRole(managerRole);


        userService.createUser(user);
//        req.getRequestDispatcher("home.jsp").forward(req, resp);

            req.getSession().setAttribute("messageSUcess", "The user : " + user.getFirstName() + "created Sucessfully");
            resp.sendRedirect("/user");




    }

}
