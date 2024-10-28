<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.devsync.model.Task" %>
<%@ page import="com.devsync.model.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manager Dashboard</title>
    <style>
        .dashboard-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        .stat-card {
            padding: 15px;
            background-color: #f5f5f5;
            border-radius: 5px;
        }
        .filters {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .task-list {
            width: 100%;
            border-collapse: collapse;
        }
        .task-list th, .task-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        .task-list th {
            background-color: #f5f5f5;
        }
        .tag {
            display: inline-block;
            padding: 2px 8px;
            margin: 2px;
            border-radius: 3px;
            background-color: #e0e0e0;
        }
        .btn-replace {
            background-color: #007bff;
            color: white;
            padding: 5px 10px;
            text-decoration: none;
            border-radius: 3px;
            font-size: 12px;
        }
        .btn-replace:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<h2>Manager Dashboard</h2>

<div class="filters">
    <form method="get">
        <label for="timeFrame">Time Frame:</label>
        <select name="timeFrame" id="timeFrame">
            <option value="week" <%= "week".equals(request.getParameter("timeFrame")) ? "selected" : "" %>>Last Week</option>
            <option value="month" <%= "month".equals(request.getParameter("timeFrame")) ? "selected" : "" %>>Last Month</option>
            <option value="year" <%= "year".equals(request.getParameter("timeFrame")) ? "selected" : "" %>>Last Year</option>
        </select>

        <label for="tag">Filter by Tag:</label>
        <select name="tag" id="tag">
            <option value="">All Tags</option>
            <%
                List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                if (tags != null) {
                    for (com.devsync.model.Tag tag : tags) {
                        boolean selected = tag.getName().equals(request.getParameter("tag"));
            %>
            <option value="<%= tag.getName() %>" <%= selected ? "selected" : "" %>><%= tag.getName() %></option>
            <%
                    }
                }
            %>
        </select>

        <button type="submit">Apply Filters</button>
    </form>
</div>

<%
    Map<String, Object> dashboardData = (Map<String, Object>) request.getAttribute("dashboardData");
    if (dashboardData != null) {
%>
<div class="dashboard-stats">
    <div class="stat-card">
        <h3>Total Tasks</h3>
        <p><%= dashboardData.get("totalTasks") %></p>
    </div>
    <div class="stat-card">
        <h3>Completed Tasks</h3>
        <p><%= dashboardData.get("completedTasks") %></p>
    </div>
    <div class="stat-card">
        <h3>Completion Rate</h3>
        <p><%= String.format("%.1f%%", dashboardData.get("completionPercentage")) %></p>
    </div>
</div>

<h3>Task List</h3>
<table class="task-list">
    <thead>
    <tr>
        <th>Title</th>
        <th>Assigned To</th>
        <th>Due Date</th>
        <th>Status</th>
        <th>Tags</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        @SuppressWarnings("unchecked")
        List<com.devsync.model.Task> tasks = (List<com.devsync.model.Task>) dashboardData.get("tasks");
        if (tasks != null && !tasks.isEmpty()) {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            for (com.devsync.model.Task task : tasks) {
    %>
    <tr>
        <td><%= task.getTitle() %></td>
        <td><%= task.getAssignedTo().getUsername() %></td>
        <td><%= dateFormat.format(task.getDueDate()) %></td>
        <td><%= task.isCompleted() ? "Completed" : "Pending" %></td>
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
        <td>
            <% if (!task.isReplacedByManager()) { %>
            <a href="<%= request.getContextPath() %>/task/replace?id=<%= task.getId() %>" class="btn-replace">Replace Task</a>
            <% } else { %>
            <span>Task Replaced</span>
            <% } %>
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
<% } %>
</body>
</html>