package com.devsync.web;

import com.devsync.model.User;
import com.devsync.service.SchedulerService;
import com.devsync.service.TaskService;
import com.devsync.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*", "/task/*"})  // Add any other paths you want to protect
public class WebFilterServlet implements Filter {
    private UserService userService;
    private SchedulerService scheduler;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService();
        scheduler = new SchedulerService(new TaskService(), userService);
        scheduler.startScheduledTasks();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();

        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (isAuthorized(user, path)) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/user");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private boolean isAuthorized(User user, String path) {
        String userRole = userService.getUserRole(user);
        if ("MANAGER".equals(userRole)) {
            return true;
        }
        return !path.equals("/user/update") && !path.equals("/user/delete") && !path.equals("/user/create");
    }

    @Override
    public void destroy() {
    }
}