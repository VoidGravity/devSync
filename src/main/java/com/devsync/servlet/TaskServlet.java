package com.devsync.servlet;

import com.devsync.model.Task;
import com.devsync.model.User;
import com.devsync.model.Tag;
import com.devsync.service.TaskService;
import com.devsync.service.TagService;
import com.devsync.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/task/*")
public class TaskServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(TaskServlet.class.getName());
    private TaskService taskService;
    private TagService tagService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskService();
        tagService = new TagService();
        userService = new UserService();

    }

    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            resp.sendRedirect(req.getContextPath() + "/login");
//            return;
//        }
//
//        User user = (User) session.getAttribute("user");
//        String path = req.getPathInfo();
//
//        if (path == null || path.equals("/")) {
//            path = "/list";
//        }
//
//        switch (path) {
//            case "/create":
//                List<Tag> allTags = tagService.getAllTags();
//                req.setAttribute("tags", allTags);
//                req.getRequestDispatcher("/WEB-INF/jsp/task/create.jsp").forward(req, resp);
//                break;
//            case "/list":
//                List<Task> tasks = taskService.getTasksForUser(user);
//                req.setAttribute("tasks", tasks);
//                req.getRequestDispatcher("/WEB-INF/jsp/listTasks.jsp").forward(req, resp);
//                break;
//            case "/edit":
//                handleEdit(req, resp, user);
//                break;
//            default:
//                resp.sendRedirect(req.getContextPath() + "/task/list");
//                break;
//        }
//    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            path = "/list";
        }

        switch (path) {
            case "/create":
                List<Tag> allTags = tagService.getAllTags();
                req.setAttribute("tags", allTags);
                req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp);
                break;
            case "/list":
                List<Task> tasks = taskService.getTasksForUser(user);
                req.setAttribute("tasks", tasks);
                // Updated path to match your structure
                req.getRequestDispatcher("/WEB-INF/jsp/listTasks.jsp").forward(req, resp);
                break;
            case "/edit":
                handleEdit(req, resp, user);
                break;
            case "/replace":
                handleReplaceGet(req, resp, user);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/task/list");
                break;
        }
    }
    private void handleEdit(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String taskId = req.getParameter("id");
        if (taskId != null) {
            Task task = taskService.getTaskById(Long.parseLong(taskId));
            if (task != null && task.getAssignedTo().getId().equals(user.getId())) {
                req.setAttribute("task", task);
                req.setAttribute("tags", tagService.getAllTags());
                req.getRequestDispatcher("/WEB-INF/jsp/editTask.jsp").forward(req, resp);
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/task/list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String path = req.getPathInfo();

        try {
            switch (path) {
                case "/create":
                    handleCreate(req, resp, user);
                    break;
                case "/update":
                    handleUpdate(req, resp, user);
                    break;
                case "/replace":
                    handleReplacePost(req, resp, user);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/task/list");
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request", e);
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            Task task = new Task();

            // Set basic task properties
            task.setTitle(req.getParameter("title"));
            task.setDescription(req.getParameter("description"));

            // Parse and set due date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(req.getParameter("dueDate"));
            task.setDueDate(dueDate);

            // Handle tags
            String[] tagIds = req.getParameterValues("tags");
            if (tagIds != null) {
                Set<Tag> tags = new HashSet<>();
                for (String tagId : tagIds) {
                    Tag tag = tagService.getTagById(Long.parseLong(tagId));
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                task.setTags(tags);
            }

            taskService.createTask(task, user);
            resp.sendRedirect(req.getContextPath() + "/task/list");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating task", e);
            req.setAttribute("error", e.getMessage());
            req.setAttribute("tags", tagService.getAllTags());
            req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp);
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            Long taskId = Long.parseLong(req.getParameter("taskId"));
            Task task = taskService.getTaskById(taskId);

            if (task == null || !task.getAssignedTo().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Task not found or access denied");
            }

            task.setTitle(req.getParameter("title"));
            task.setDescription(req.getParameter("description"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(req.getParameter("dueDate"));
            task.setDueDate(dueDate);

            String[] tagIds = req.getParameterValues("tags");
            if (tagIds != null) {
                Set<Tag> tags = new HashSet<>();
                for (String tagId : tagIds) {
                    Tag tag = tagService.getTagById(Long.parseLong(tagId));
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                task.setTags(tags);
            }

            task.setCompleted(Boolean.parseBoolean(req.getParameter("completed")));

            taskService.updateTask(task, user);
            resp.sendRedirect(req.getContextPath() + "/task/list");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating task", e);
            req.setAttribute("error", e.getMessage());
            handleEdit(req, resp, user);
        }
    }
    private void handleReplaceGet(HttpServletRequest req, HttpServletResponse resp, User manager) throws ServletException, IOException {
//        if (!"MANAGER".equals(manager.getManagerRole())) {
//            resp.sendRedirect(req.getContextPath() + "/task/list");
//            return;
//        }

        String taskId = req.getParameter("id");
        if (taskId != null) {
            Task task = taskService.getTaskById(Long.parseLong(taskId));
            if (task != null && !task.isReplacedByManager()) {
                req.setAttribute("oldTask", task);
                req.setAttribute("tags", tagService.getAllTags());
                req.setAttribute("users", userService.getUsers()); // Changed to getUsers()
                req.getRequestDispatcher("/WEB-INF/jsp/replaceTask.jsp").forward(req, resp);
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/app/manager/dashboard");
    }

    private void handleReplacePost(HttpServletRequest req, HttpServletResponse resp, User manager) throws ServletException, IOException {
//        if (!"MANAGER".equals(manager.getManagerRole())) {
//            resp.sendRedirect(req.getContextPath() + "/task/list");
//            return;
//        }

        try {
            // Get the old task
            Long oldTaskId = Long.parseLong(req.getParameter("oldTaskId"));
            Task oldTask = taskService.getTaskById(oldTaskId);

            // Create the new task
            Task newTask = new Task();
            newTask.setTitle(req.getParameter("title"));
            newTask.setDescription(req.getParameter("description"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            newTask.setDueDate(dateFormat.parse(req.getParameter("dueDate")));

            // Get the new assignee - using Long.parseLong() instead of casting to int
//            int userId = Long.parseLong(req.getParameter("assignedTo"));
            User newAssignee = userService.getUserById(Integer.parseInt(req.getParameter("assignedTo")));
//            User newAssignee = userService.getUserById(Long.valueOf(req.getParameter("assignedTo")));

            // Handle tags
            Set<Tag> tags = new HashSet<>();
            String[] tagIds = req.getParameterValues("tags");
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.getTagById(Long.parseLong(tagId));
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
            }
            newTask.setTags(tags);

            taskService.replaceTask(oldTask, newTask, newAssignee, manager);
            resp.sendRedirect(req.getContextPath() + "/app/manager/dashboard");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error replacing task", e);
            req.setAttribute("error", e.getMessage());
            handleReplaceGet(req, resp, manager);
        }
    }
    }