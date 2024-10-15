package com.devsync.service;

import com.devsync.dao.TaskDAO;
import com.devsync.dao.UserDAO;
import com.devsync.model.Task;
import com.devsync.model.User;

import java.util.Date;
import java.util.List;

public class TaskService {

    public void createTask(Task task, User user) throws IllegalArgumentException {
        Date currentDate = new Date();
        if (task.getDueDate().before(currentDate)) {
            throw new IllegalArgumentException("Task cannot be created in the past");
        }

        Date threeDaysLater = new Date(currentDate.getTime() + (3 * 24 * 60 * 60 * 1000));
        if (task.getDueDate().after(threeDaysLater)) {
            throw new IllegalArgumentException("Task cannot be scheduled more than 3 days in advance");
        }

        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }

        task.setCreationDate(currentDate);
        task.setCreatedBy(user);
        task.setAssignedTo(user);
        task.setCompleted(false);

        TaskDAO.create(task);
    }

    public void updateTask(Task task, User user) throws IllegalArgumentException {
        Task existingTask = TaskDAO.findTask(task.getId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found");
        }

        if (!existingTask.getAssignedTo().getId().equals(user.getId()) && !user.getManagerRole().equals("MANAGER")) {
            throw new IllegalArgumentException("You don't have permission to update this task");
        }

        if (task.getDueDate().before(new Date())) {
            throw new IllegalArgumentException("Task due date cannot be set in the past");
        }

        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }

        if (task.isCompleted() && task.getDueDate().before(new Date())) {
            throw new IllegalArgumentException("Task cannot be marked as completed after the deadline");
        }

        if (!existingTask.getCreatedBy().getId().equals(user.getId()) && user.getModificationTokens() <= 0) {
            throw new IllegalArgumentException("You don't have enough modification tokens");
        }

        TaskDAO.updateTask(task);

        if (!existingTask.getCreatedBy().getId().equals(user.getId())) {
            user.setModificationTokens(user.getModificationTokens() - 1);
            UserDAO.updateUser(user);
        }
    }

    public void deleteTask(Task task, User user) throws IllegalArgumentException {
        Task existingTask = TaskDAO.findTask(task.getId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found");
        }

        if (!existingTask.getCreatedBy().getId().equals(user.getId()) && !user.getManagerRole().equals("MANAGER")) {
            if (user.getDeletionTokens() <= 0) {
                throw new IllegalArgumentException("You don't have enough deletion tokens");
            }
            user.setDeletionTokens(user.getDeletionTokens() - 1);
            UserDAO.updateUser(user);
        }

        TaskDAO.deleteTask(task);
    }

   

    public void markOverdueTasks() {
        List<Task> overdueTasks = TaskDAO.getOverdueTasks();
        for (Task task : overdueTasks) {
            task.setCompleted(false);
            TaskDAO.updateTask(task);
        }
    }
    public Task getTaskById(Long id) {
        return TaskDAO.findTask(id);
    }
    public List<Task> getTasksForUser(User user) {
        return TaskDAO.getTasksForUser(user);
    }

}