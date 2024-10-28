<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Task</title>
    <style>
        .error { color: red; margin-bottom: 10px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], textarea { width: 100%; padding: 8px; }
        .tag-group { display: flex; flex-wrap: wrap; gap: 10px; }
        .tag-checkbox { display: flex; align-items: center; gap: 5px; }
    </style>
</head>
<body>
<h2>Create New Task</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="error"><%= request.getAttribute("error") %></div>
<% } %>

<form action="<%= request.getContextPath() %>/task/create" method="post">
    <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required>
    </div>

    <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="4"></textarea>
    </div>

    <div class="form-group">
        <label for="dueDate">Due Date:</label>
        <input type="date" id="dueDate" name="dueDate" required>
    </div>

    <div class="form-group">
        <label>Tags (select at least 2):</label>
        <div class="tag-group">
            <%
                List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                if (tags != null) {
                    for (Tag tag : tags) {
            %>
            <div class="tag-checkbox">
                <input type="checkbox" name="tags" value="<%= tag.getId() %>" id="tag<%= tag.getId() %>">
                <label for="tag<%= tag.getId() %>"><%= tag.getName() %></label>
            </div>
            <%
                    }
                }
            %>
        </div>
    </div>

    <button type="submit">Create Task</button>
    <a href="<%= request.getContextPath() %>/task/list">Cancel</a>
</form>
</body>
</html>