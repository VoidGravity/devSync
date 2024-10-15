package com.devsync.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TaskService taskService;
    private final UserService userService;

    public SchedulerService(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public void startScheduledTasks() {
        // Run every 24 hours
        scheduler.scheduleAtFixedRate(this::dailyTasks, 0, 24, TimeUnit.HOURS);
    }

    private void dailyTasks() {
        taskService.markOverdueTasks();
        userService.processChangeRequests();
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}