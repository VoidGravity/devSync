<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.devsync.model.Task" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Task</title>
    <style>
        .error { color: red; margin-bottom: 10px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], textarea { width: 100%; padding: 8px; }
        .tag-group { display: flex; flex-wrap: wrap; gap: 10px; }
        .tag-checkbox { display: flex; align-items: center; gap: 5px; }
        .btn { padding: 8px 16px; margin-right: 10px; }
    </style>
</head>
<body>
<h2>Edit Task</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="error"><%= request.getAttribute("error") %></div>
<% } %>

<%
    Task task = (Task) request.getAttribute("task");
    if (task != null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<form action="<%= request.getContextPath() %>/task/update" method="post">
    <input type="hidden" name="taskId" value="<%= task.getId() %>">

    <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="<%= task.getTitle() %>" required>
    </div>

    <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="4"><%= task.getDescription() != null ? task.getDescription() : "" %></textarea>
    </div>

    <div class="form-group">
        <label for="dueDate">Due Date:</label>
        <input type="date" id="dueDate" name="dueDate" value="<%= dateFormat.format(task.getDueDate()) %>" required>
    </div>

    <div class="form-group">
        <label>Tags (select at least 2):</label>
        <div class="tag-group">
            <%
                List<Tag> allTags = (List<Tag>) request.getAttribute("tags");
                if (allTags != null) {
                    Set<Tag> taskTags = task.getTags();
                    for (Tag tag : allTags) {
                        boolean isChecked = taskTags != null && taskTags.stream()
                                .anyMatch(t -> t.getId().equals(tag.getId()));
            %>
            <div class="tag-checkbox">
                <input type="checkbox" name="tags" value="<%= tag.getId() %>"
                       id="tag<%= tag.getId() %>" <%= isChecked ? "checked" : "" %>>
                <label for="tag<%= tag.getId() %>"><%= tag.getName() %></label>
            </div>
            <%
                    }
                }
            %>
        </div>
    </div>

    <div class="form-group">
        <label for="completed">Status:</label>
        <input type="checkbox" id="completed" name="completed" value="true" <%= task.isCompleted() ? "checked" : "" %>>
        <label for="completed" style="display: inline;">Mark as completed</label>
    </div>

    <button type="submit" class="btn">Update Task</button>
    <a href="<%= request.getContextPath() %>/task/list" class="btn">Cancel</a>
</form>
<% } else { %>
<p>Task not found.</p>
<a href="<%= request.getContextPath() %>/task/list">Back to List</a>
<% } %>

<script>
    // Client-side validation
    document.querySelector('form').addEventListener('submit', function(e) {
        const checkedTags = document.querySelectorAll('input[name="tags"]:checked').length;
        if (checkedTags < 2) {
            e.preventDefault();
            alert('Please select at least 2 tags');
        }

        const dueDate = new Date(document.getElementById('dueDate').value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (dueDate < today) {
            e.preventDefault();
            alert('Due date cannot be in the past');
        }
    });
</script>
</body>
</html>