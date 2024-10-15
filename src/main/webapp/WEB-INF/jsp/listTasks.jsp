<%@ page import="com.devsync.model.Task" %>
<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .action-buttons a {
            margin-right: 10px;
            text-decoration: none;
            padding: 5px 10px;
            border-radius: 3px;
        }
        .edit-btn {
            background-color: #4CAF50;
            color: white;
        }
        .delete-btn {
            background-color: #f44336;
            color: white;
        }
        .create-btn {
            background-color: #008CBA;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            display: inline-block;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<h1>Your Tasks</h1>

<a href="${pageContext.request.contextPath}/task/create" class="create-btn">Create New Task</a>

<table>
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Due Date</th>
        <th>Tags</th>
        <th>Actions</th>
    </tr>
    <%
        List<Task> tasks = (List<Task>)request.getAttribute("tasks");
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
    %>
    <tr>
        <td><%= task.getTitle() %></td>
        <td><%= task.getDescription() %></td>
        <td><%= task.getDueDate() %></td>
        <td>
            <%
                Set<Tag> tags = task.getTags();
                if (tags != null && !tags.isEmpty()) {
                    boolean first = true;
                    for (Tag tag : tags) {
                        if (!first) {
                            out.print(", ");
                        }
                        out.print(tag.getName());
                        first = false;
                    }
                } else {
                    out.print("No tags");
                }
            %>
        </td>
        <td class="action-buttons">
            <a href="${pageContext.request.contextPath}/task/edit?id=<%= task.getId() %>" class="edit-btn">Edit</a>
            <a href="${pageContext.request.contextPath}/task/delete?id=<%= task.getId() %>" class="delete-btn" onclick="return confirm('Are you sure you want to delete this task?');">Delete</a>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">No tasks found</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>