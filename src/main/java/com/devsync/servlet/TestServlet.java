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
            case "/edit":
                User user = new User();

                user.setId(Integer.parseInt(req.getParameter("id")));
                user=userService.findUser(user);
                req.setAttribute("user",user);
                req.getRequestDispatcher("/WEB-INF/jsp/editUser.jsp").forward(req,resp);
                break;
            case "/delete":
                User myuser = new User();
                myuser.setId(Integer.parseInt(req.getParameter("id")));

                userService.deleteUser(myuser);
                resp.sendRedirect("/user");
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();

        String path = req.getPathInfo();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String email = req.getParameter("email");
        String role =req.getParameter("manager_role");
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(fname);
        user.setLastName(lname);
        user.setEmail(email);
        if(role.equals("MANAGER")){
            ManagerRole managerRole = ManagerRole.MANAGER;
            user.setManagerRole(managerRole);

        }else{
            ManagerRole managerRole = ManagerRole.TEAM_LEAD;
            user.setManagerRole(managerRole);

        }


        switch (path){
            case "/update":
                int id = Integer.parseInt(req.getParameter("id"));
                user.setId(id);
                userService.updateUser(user);

                req.getSession().setAttribute("msucess","The user : " +user.getUsername()+" was updated successfully");
                resp.sendRedirect("/user");

                break;
            case "/create":

                userService.createUser(user);

                req.getSession().setAttribute("messageSUcess", "The user : " + user.getUsername() + " was created Sucessfully");
                resp.sendRedirect("/user");
                break;
        }



    }

}
