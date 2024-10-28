<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.devsync.model.Task" %>
<%@ page import="com.devsync.model.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task List</title>
    <style>
        .task-list { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .task-list th, .task-list td { padding: 10px; text-align: left; border: 1px solid #ddd; }
        .task-list th { background-color: #f5f5f5; }
        .tag { display: inline-block; padding: 2px 8px; margin: 2px; border-radius: 3px; background-color: #e0e0e0; }
        .completed { background-color: #e8f5e9; }
        .overdue { background-color: #ffebee; }
        .create-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .error { color: red; margin: 10px 0; }
    </style>
</head>
<body>
<h2>My Tasks</h2>

<a href="<%= request.getContextPath() %>/task/create" class="create-button">Create New Task</a>

<% if (request.getAttribute("error") != null) { %>
<div class="error"><%= request.getAttribute("error") %></div>
<% } %>

<table class="task-list">
    <thead>
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Due Date</th>
        <th>Tags</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<Task> tasks = (List<Task>) request.getAttribute("tasks");
        if (tasks != null && !tasks.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();

            for (Task task : tasks) {
                boolean isOverdue = task.getDueDate().before(now) && !task.isCompleted();
                String rowClass = task.isCompleted() ? "completed" : (isOverdue ? "overdue" : "");
    %>
    <tr class="<%= rowClass %>">
        <td><%= task.getTitle() %></td>
        <td><%= task.getDescription() != null ? task.getDescription() : "" %></td>
        <td><%= dateFormat.format(task.getDueDate()) %></td>
        <td>
            <%
                if (task.getTags() != null) {
                    for (com.devsync.model.Tag tag : task.getTags()) {
            %>
            <span class="tag"><%= tag.getName() %></span>
            <%
                    }
                }
            %>
        </td>
        <td><%= task.isCompleted() ? "Completed" : "Pending" %></td>
        <td>
            <a href="<%= request.getContextPath() %>/task/edit?id=<%= task.getId() %>">Edit</a>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="6">No tasks found</td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>