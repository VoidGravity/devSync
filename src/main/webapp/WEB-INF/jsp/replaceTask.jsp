<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.devsync.model.User" %>
<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Replace Task</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <h2>Replace Task</h2>
  <form action="${pageContext.request.contextPath}/task/replace" method="post">
    <input type="hidden" name="oldTaskId" value="${oldTask.id}">

    <div class="mb-3">
      <label for="title" class="form-label">New Task Title</label>
      <input type="text" class="form-control" id="title" name="title" required>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">New Task Description</label>
      <textarea class="form-control" id="description" name="description" rows="3"></textarea>
    </div>
    <div class="mb-3">
      <label for="dueDate" class="form-label">New Due Date</label>
      <input type="date" class="form-control" id="dueDate" name="dueDate" required>
    </div>
    <div class="mb-3">
      <label for="assignedTo" class="form-label">Assign To</label>
      <select class="form-select" id="assignedTo" name="assignedTo" required>
        <%
          List<User> users = (List<User>)request.getAttribute("users");
          if (users != null) {
            for (User user : users) {
        %>
        <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
        <%
            }
          }
        %>
      </select>
    </div>
    <div class="mb-3">
      <label class="form-label">Tags (select at least 2)</label>
      <select class="form-select" name="tags" multiple required>
        <%
          List<Tag> tags = (List<Tag>)request.getAttribute("tags");
          if (tags != null) {
            for (Tag tag : tags) {
        %>
        <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
        <%
            }
          }
        %>
      </select>
    </div>
    <button type="submit" class="btn btn-primary">Replace Task</button>
  </form>
</div>
</body>
</html>