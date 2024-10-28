<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.devsync.model.Task" %>
<%@ page import="com.devsync.model.Tag" %>
<%@ page import="com.devsync.model.User" %>
<!DOCTYPE html>
<html>
<head>
  <title>Replace Task</title>
  <style>
    .form-group { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; }
    input[type="text"], textarea, select { width: 100%; padding: 8px; }
    .error { color: red; margin: 10px 0; }
    .tag-group { display: flex; flex-wrap: wrap; gap: 10px; }
    .tag-checkbox { display: flex; align-items: center; gap: 5px; }
  </style>
</head>
<body>
<h2>Replace Task</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="error"><%= request.getAttribute("error") %></div>
<% } %>

<%
  Task oldTask = (Task) request.getAttribute("oldTask");
  List<User> users = (List<User>) request.getAttribute("users");
  if (oldTask != null) {
%>
<div class="old-task-info">
  <h3>Original Task Details:</h3>
  <p><strong>Title:</strong> <%= oldTask.getTitle() %></p>
  <p><strong>Assigned To:</strong> <%= oldTask.getAssignedTo().getUsername() %></p>
  <p><strong>Due Date:</strong> <%= new SimpleDateFormat("yyyy-MM-dd").format(oldTask.getDueDate()) %></p>
</div>

<form action="<%= request.getContextPath() %>/task/replace" method="post">
  <input type="hidden" name="oldTaskId" value="<%= oldTask.getId() %>">

  <div class="form-group">
    <label for="title">New Title:</label>
    <input type="text" id="title" name="title" required>
  </div>

  <div class="form-group">
    <label for="description">New Description:</label>
    <textarea id="description" name="description" rows="4"></textarea>
  </div>

  <div class="form-group">
    <label for="dueDate">New Due Date:</label>
    <input type="date" id="dueDate" name="dueDate" required>
  </div>

  <div class="form-group">
    <label for="assignedTo">Assign To:</label>
    <select id="assignedTo" name="assignedTo" required>
      <option value="">Select User</option>
      <%
        if (users != null) {
          for (User user : users) {
            if (!user.getId().equals(oldTask.getAssignedTo().getId())) {
      %>
      <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
      <%
            }
          }
        }
      %>
    </select>
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
        <input type="checkbox" name="tags" value="<%= tag.getId() %>"
               id="tag<%= tag.getId() %>">
        <label for="tag<%= tag.getId() %>"><%= tag.getName() %></label>
      </div>
      <%
          }
        }
      %>
    </div>
  </div>

  <button type="submit">Replace Task</button>
  <a href="<%= request.getContextPath() %>/app/manager/dashboard">Cancel</a>
</form>
<% } %>
</body>
</html>