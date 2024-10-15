<%@ page import="com.devsync.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Task</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Create New Task</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>
    <form action="/task/create" method="post">
        <div class="mb-3">
            <label for="title" class="form-label">Title</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
        </div>
        <div class="mb-3">
            <label for="dueDate" class="form-label">Due Date</label>
            <input type="date" class="form-control" id="dueDate" name="dueDate" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Tags (select at least 2)</label>
            <select class="form-select" name="tags" multiple required>
                <%
                    List<Tag> tags = (List<Tag>) request.getAttribute("tags");
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
        <button type="submit" class="btn btn-primary">Create Task</button>
    </form>
</div>
</body>
</html>