package com.devsync.servlet;

import com.devsync.model.User;
import com.devsync.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth_login")
public class AuthServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User authenticatedUser = userService.login(username, password);

        if (authenticatedUser != null) {
            // Authentication successful
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authenticatedUser);
            // Redirect to the user's dashboard or home page
            response.sendRedirect(request.getContextPath() + "/dash");
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/").forward(request, response);
        }
    }
}