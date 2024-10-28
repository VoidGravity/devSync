package com.devsync.servlet;

import com.devsync.model.User;
import com.devsync.model.Tag;
import com.devsync.service.TaskService;
import com.devsync.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.List;

@WebServlet("/app/manager/dashboard")
public class ManagerDashboardServlet extends HttpServlet {
    private TaskService taskService;
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskService();
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User manager = (User) req.getSession().getAttribute("user");
        String timeFrame = req.getParameter("timeFrame");
        String tagName = req.getParameter("tag");

//        if (manager == null || !"MANAGER".equals(manager.getManagerRole())) {
//            resp.sendRedirect(req.getContextPath() + "/");
//            return;
//        }
        if (manager == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // Provide default values if parameters are not present
        if (timeFrame == null) {
            timeFrame = "month"; // Default to month view
        }
        if (tagName == null) {
            tagName = ""; // Default to no tag filter
        }

        Map<String, Object> dashboardData = taskService.getManagerDashboard(manager, timeFrame, tagName);
        req.setAttribute("dashboardData", dashboardData);

        List<Tag> tags = tagService.getAllTags();
        req.setAttribute("tags", tags);

        req.getRequestDispatcher("/WEB-INF/jsp/managerDashboard.jsp").forward(req, resp);
    }
}