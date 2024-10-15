package com.devsync.servlet;

import com.devsync.model.Task;
import com.devsync.model.User;
import com.devsync.model.Tag;
import com.devsync.service.TaskService;
import com.devsync.service.UserService;
import com.devsync.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/task/*")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private UserService userService;
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskService();
        userService = new UserService();
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        User user = (User) session.getAttribute("user");
        String path = req.getPathInfo();

        if (path == null) {
            path = "/list"; // Default to list if no specific path is provided
        }

        switch (path) {
            case "/create":
                List<Tag> allTags = tagService.getAllTags();
                req.setAttribute("tags", allTags);
                req.getRequestDispatcher("/WEB-INF/jsp/createTask.jsp").forward(req, resp);
                break;
            case "/list":
                List<Task> tasks = taskService.getTasksForUser(user);
                req.setAttribute("tasks", tasks);
                req.getRequestDispatcher("/WEB-INF/jsp/listTasks.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/task/list");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        User user = (User) session.getAttribute("user");
        String path = req.getPathInfo();

        if (path == null) {
            path = "/list";
        }

        switch (path) {
            case "/list":
                List<Task> tasks = taskService.getTasksForUser(user);
                req.setAttribute("tasks", tasks);
                req.getRequestDispatcher("/WEB-INF/jsp/listTasks.jsp").forward(req, resp);
                break;
            case "/create":
                createTask(req, resp, user);
                break;
            case "/update":
                updateTask(req, resp, user);
                break;
            case "/delete":
                deleteTask(req, resp, user);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/task/list");
                break;
        }
    }

    private void createTask(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            Task task = new Task();
            task.setTitle(req.getParameter("title"));
            task.setDescription(req.getParameter("description"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(req.getParameter("dueDate"));
            task.setDueDate(dueDate);

            String[] tagIds = req.getParameterValues("tags");
            if (tagIds != null && tagIds.length >= 2) {
                Set<Tag> tags = new HashSet<>();
                for (String tagId : tagIds) {
                    Tag tag = tagService.getTagById(Long.parseLong(tagId));
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                task.setTags(tags);
            } else {
                throw new IllegalArgumentException("At least two tags are required");
            }

            taskService.createTask(task, user);
            resp.sendRedirect(req.getContextPath() + "/task/list");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            List<Tag> allTags = tagService.getAllTags();
            req.setAttribute("tags", allTags);
            req.getRequestDispatcher("/WEB-INF/jsp/createTask.jsp").forward(req, resp);
        }
    }

    private void updateTask(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            Long taskId = Long.parseLong(req.getParameter("taskId"));
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                throw new IllegalArgumentException("Task not found");
            }

            task.setTitle(req.getParameter("title"));
            task.setDescription(req.getParameter("description"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(req.getParameter("dueDate"));
            task.setDueDate(dueDate);

            String[] tagIds = req.getParameterValues("tags");
            if (tagIds != null && tagIds.length >= 2) {
                Set<Tag> tags = new HashSet<>();
                for (String tagId : tagIds) {
                    Tag tag = tagService.getTagById(Long.parseLong(tagId));
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                task.setTags(tags);
            } else {
                throw new IllegalArgumentException("At least two tags are required");
            }

            task.setCompleted(Boolean.parseBoolean(req.getParameter("completed")));

            taskService.updateTask(task, user);
            resp.sendRedirect(req.getContextPath() + "/task/list");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("task", taskService.getTaskById(Long.parseLong(req.getParameter("taskId"))));
            List<Tag> allTags = tagService.getAllTags();
            req.setAttribute("tags", allTags);
            req.getRequestDispatcher("/WEB-INF/jsp/editTask.jsp").forward(req, resp);
        }
    }

    private void deleteTask(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            Long taskId = Long.parseLong(req.getParameter("taskId"));
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                throw new IllegalArgumentException("Task not found");
            }

            taskService.deleteTask(task, user);
            resp.sendRedirect(req.getContextPath() + "/task/list");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}