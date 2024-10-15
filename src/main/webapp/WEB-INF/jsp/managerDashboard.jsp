<%@ page import="com.devsync.model.Task" %>
<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager Dashboard</title>
</head>
<body>
<h1>Manager Dashboard</h1>

<form action="<%=request.getContextPath()%>/app/manager/dashboard" method="get">
    <select name="timeFrame">
        <option value="week">This Week</option>
        <option value="month">This Month</option>
        <option value="year">This Year</option>
    </select>
    <select name="tag">
        <%
            List<Tag> tags = (List<Tag>) request.getAttribute("tags");
            if (tags != null) {
                for (Tag tag : tags) {
        %>
        <option value="<%= tag.getName() %>"><%= tag.getName() %></option>
        <%
                }
            }
        %>
    </select>
    <input type="submit" value="Filter">
</form>

<%
    Map<String, Object> dashboardData = (Map<String, Object>) request.getAttribute("dashboardData");
    if (dashboardData != null) {
%>
<h2>Task Completion</h2>
<p>Completion Percentage: <%= dashboardData.get("completionPercentage") %>%</p>

<h2>Tasks</h2>
<table border="1">
    <tr>
        <th>Title</th>
        <th>Assigned To</th>
        <th>Due Date</th>
        <th>Status</th>
    </tr>
    <%
        List<Task> tasks = (List<Task>) dashboardData.get("tasks");
        if (tasks != null) {
            for (Task task : tasks) {
    %>
    <tr>
        <td><%= task.getTitle() %></td>
        <td><%= task.getAssignedTo().getUsername() %></td>
        <td><%= task.getDueDate() %></td>
        <td><%= task.isCompleted() ? "Completed" : "Pending" %></td>
    </tr>
    <%
            }
        }
    %>
</table>
<%
    }
%>
</body>
</html>