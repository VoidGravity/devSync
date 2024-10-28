package com.devsync.service;

import com.devsync.dao.TaskDAO;
import com.devsync.model.Task;
import com.devsync.model.User;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.devsync.dao.UserDAO;

public class TaskService {
    private static final Logger logger = Logger.getLogger(TaskService.class.getName());

    private UserDAO userDAO;

    public TaskService() {
        this.userDAO = new UserDAO();
    }

    public void createTask(Task task, User user) throws IllegalArgumentException {
        logger.info("Starting task creation validation");

        validateTaskCreation(task);

        task.setCreationDate(new Date());
        task.setCreatedBy(user);
        task.setAssignedTo(user);
        task.setCompleted(false);

        try {
            TaskDAO.createTask(task);
            logger.info("Task created successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in createTask", e);
            throw new IllegalArgumentException("Failed to create task: " + e.getMessage());
        }
    }
    public void markOverdueTasks() {
        try {
            List<Task> overdueTasks = TaskDAO.getOverdueTasks();
            for (Task task : overdueTasks) {
                if (!task.isCompleted()) {
                    task.setCompleted(false);
                    TaskDAO.updateTask(task);
                }
            }
            logger.info("Marked " + overdueTasks.size() + " tasks as overdue");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error marking overdue tasks", e);
            throw new RuntimeException("Failed to mark overdue tasks", e);
        }
    }

    public Map<String, Object> getManagerDashboard(User manager, String timeFrame, String tagName) {
        try {
            if (manager == null) {
                throw new IllegalArgumentException("Manager cannot be null");
            }

            List<Task> tasks = TaskDAO.getTasksForManagerDashboard(manager, timeFrame, tagName);

            int completedTasks = 0;
            int totalTasks = tasks.size();

            for (Task task : tasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                }
            }

            double completionPercentage = totalTasks > 0 ?
                    (completedTasks * 100.0) / totalTasks : 0;

            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("tasks", tasks);
            dashboardData.put("completionPercentage", completionPercentage);
            dashboardData.put("totalTasks", totalTasks);
            dashboardData.put("completedTasks", completedTasks);

            return dashboardData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating manager dashboard", e);
            throw new RuntimeException("Failed to generate manager dashboard: " + e.getMessage(), e);
        }
    }

    private void validateTaskCreation(Task task) {
        Date currentDate = new Date();

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        if (task.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        if (task.getDueDate().before(currentDate)) {
            throw new IllegalArgumentException("Task cannot be created in the past");
        }

        Date threeDaysLater = new Date(currentDate.getTime() + (3 * 24 * 60 * 60 * 1000L));
        if (task.getDueDate().after(threeDaysLater)) {
            throw new IllegalArgumentException("Task cannot be scheduled more than 3 days in advance");
        }

        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }
    }

    public List<Task> getTasksForUser(User user) {
        return TaskDAO.getTasksForUser(user);
    }

    public Task getTaskById(Long id) {
        return TaskDAO.findTask(id);
    }
    public void replaceTask(Task oldTask, Task newTask, User newAssignee, User manager) {
        if (!"MANAGER".equals(manager.getManagerRole())) {
            throw new IllegalArgumentException("Only managers can replace tasks");
        }

        // Mark old task as replaced and non-modifiable
        oldTask.setReplacedByManager(true);
        oldTask.setModifiable(false);
        TaskDAO.updateTask(oldTask);

        // Set up new task
        newTask.setAssignedTo(newAssignee);
        newTask.setCreatedBy(manager);
        newTask.setCreationDate(new Date());
        newTask.setModifiable(true);
        newTask.setReplacedByManager(false);
        TaskDAO.createTask(newTask);

        logger.info(String.format("Task %d replaced with new task %d by manager %s",
                oldTask.getId(), newTask.getId(), manager.getUsername()));
    }
    public void updateTask(Task task, User user) {
        Task existingTask = TaskDAO.findTask(task.getId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Check if task is modifiable
        if (!existingTask.isModifiable()) {
            throw new IllegalArgumentException("This task cannot be modified as it was replaced by a manager");
        }

        // Check if user owns the task
        boolean isOwnTask = existingTask.getAssignedTo().getId().equals(user.getId());

        // If not own task, check modification tokens
        if (!isOwnTask) {
            if (user.getModificationTokens() <= 0) {
                throw new IllegalArgumentException("You don't have enough modification tokens");
            }
            // Deduct token only if it's not the user's own task
            user.setModificationTokens(user.getModificationTokens() - 1);
            userDAO.updateUser(user);
        }

        validateTaskUpdate(task);
        TaskDAO.updateTask(task);
    }

    private void validateTaskUpdate(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        if (task.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        if (task.getDueDate().before(new Date())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }
    }
    public void deleteTask(Task task, User user) {
        Task existingTask = TaskDAO.findTask(task.getId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Check if task is modifiable
        if (!existingTask.isModifiable()) {
            throw new IllegalArgumentException("This task cannot be deleted as it was replaced by a manager");
        }

        // Check if user owns the task
        boolean isOwnTask = existingTask.getCreatedBy().getId().equals(user.getId());

        // If not own task, check deletion tokens
        if (!isOwnTask) {
            if (user.getDeletionTokens() <= 0) {
                throw new IllegalArgumentException("You don't have enough deletion tokens");
            }
            // Deduct token only if it's not the user's own task
            user.setDeletionTokens(user.getDeletionTokens() - 1);
            userDAO.updateUser(user);
        }

        TaskDAO.deleteTask(task);
    }

}