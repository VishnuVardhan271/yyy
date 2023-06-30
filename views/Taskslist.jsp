<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.pennant.prodmtr.model.Entity.User" %>
<%@ page import="javax.servlet.http.HttpSession" %>


<html>
<head>
    <title>Tasks</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    
<center><h1>Tasks List</h1></center>
    <!-- Add filters for project-wise and resource-wise views -->
    <form id="filterForm">
        <label for="projectId">Project Id:</label>
        <select name="projectId" id="projectId">
            <option value="">All</option>
            <!-- Populate project options -->
            <c:set var="uniqueProjectIds" value="[]" />
            <c:forEach items="${tasks}" var="task">
                <c:if test="${!fn:contains(uniqueProjectIds, task.projectId)}">
                    <c:set var="uniqueProjectIds" value="${uniqueProjectIds},${task.projectId}" />
                    <option value="${task.projectId}">${task.projectId}</option>
                </c:if>
            </c:forEach>
        </select>

        <label for="taskStatus">Task Status:</label>
        <select name="taskStatus" id="taskStatus">
            <option value="">All</option>
            <!-- Populate task status options -->
            <c:set var="uniqueTaskStatus" value="[]" />
            <c:forEach items="${tasks}" var="task">
                <c:if test="${!fn:contains(uniqueTaskStatus, task.taskStatus)}">
                    <c:set var="uniqueTaskStatus" value="${uniqueTaskStatus},${task.taskStatus}" />
                    <option value="${task.taskStatus}">${task.taskStatus}</option>
                </c:if>
            </c:forEach>
        </select>
       <%  
       String ut = (String) request.getAttribute("Task");
       User u=(User)session.getAttribute("user"); 
         int userId=u.getUserId();
       if(ut != null && !ut.equals("User"))
    		   
{
    		   %>
        <label for="taskSupervisor">Task Supervisor:</label>
        <select name="taskSupervisorId" id="taskSupervisorId">
            <option value="">All</option>
            <!-- Populate task supervisor options -->
            <c:set var="uniqueSupervisorIds" value="[]" />
            <c:forEach items="${tasks}" var="task">
                <c:if test="${!fn:contains(uniqueSupervisorIds, task.taskSupervisorId)}">
                    <c:set var="uniqueSupervisorIds" value="${uniqueSupervisorIds},${task.taskSupervisorId}" />
                    <option value="${task.taskSupervisorId}">${task.taskSupervisorId}</option>
                </c:if>
            </c:forEach>
        </select>
     <% 
}else { %>
    <input type="hidden" name="taskSupervisorId" value="<%= userId %>">
<% } %>

        <button type="submit">Apply Filters</button>
    </form>

    <table id="taskTable">
        <tr>
            <th>Task ID</th>
            <th>Task Name</th>
            <th>Task Supervisor</th>
            <th>Task Status</th>
            <th>Actions</th>
        </tr>
        <c:forEach items="${tasks}" var="task">
           <tr class="task-row" onclick="window.location.href='taskdetailsbyid?taskId=${task.taskId}'">

                <td>${task.taskId}</td>
                <td>${task.taskName}</td>
                <td>${task.taskSupervisorId}</td>
                <td>${task.taskStatus}</td>
                <td>
                    <c:if test="${task.taskStatus == 'INPR' || task.taskStatus == 'REFC' || task.taskStatus == 'REVW' }">
                        <form action="updateTaskStatus" method="GET" style="display: inline;">
                            <input type="hidden" name="taskId" value="${task.taskId}" />
                            <button type="submit" class="btn btn-primary update-button">Update Status</button>
                        </form>
                        <form action="createSubtask" method="GET" style="display: inline;">
                            <input type="hidden" name="taskId" value="${task.taskId}" />
                            <button type="submit" class="btn btn-success create-button">Create Subtask</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
  <script>
$(document).ready(function() {
    $('#filterForm').on('submit', function(event) {
        event.preventDefault();
        var formData = $(this).serialize();
        
        console.log(JSON.stringify(formData));
        $.ajax({
            url: "Taskfilter",
            type: "POST",
            data: formData,
            dataType: "json",
            success: function(response) {
                var tbody = $("#taskTable tbody");
                tbody.empty(); // Clear existing rows

                // Iterate over the filtered tasks and add them to the table
                for (var i = 0; i < response.length; i++) {
                    var task = response[i];
                    var row =
                    	'<tr  onclick="window.location.href=\'taskdetailsbyid?taskId=' + task.taskId + '\';">' +
                        "<td>" + task.taskId + "</td>" +
                        "<td>" + task.taskName + "</td>" +
                        "<td>" + task.taskSupervisorId + "</td>" +
                        "<td>" + task.taskStatus + "</td>" +
                        "<td>";

                    if (task.taskStatus === "INPR" || task.taskStatus === "REFC" || task.taskStatus === "REVW") {
                        row +=
                            '<form action="updateTaskStatus" method="GET" style="display: inline;">' +
                            '<input type="hidden" name="taskId" value="' +
                            task.taskId +
                            '" />' +
                            '<button type="submit" class="btn btn-primary update-button">Update Status</button>' +
                            "</form>" +
                            '<form action="createSubtask" method="GET" style="display: inline;">' +
                            '<input type="hidden" name="taskId" value="' +
                            task.taskId +
                            '" />' +
                            '<button type="submit" class="btn btn-success create-button">Create Subtask</button>' +
                            "</form>";
                    }

                    row += "</td>" + "</tr>";

                    tbody.append(row);
                }
            },
            error: function() {
                alert("An error occurred while retrieving filtered tasks.");
            }
        });
    });
});
</script>


<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f2f2f2;
    }

    h1 {
        color: #333;
        text-align: center;
    }

    form {
        text-align: center;
        margin-bottom: 20px;
    }

    select,
    button {
        padding: 8px 12px;
        border: none;
        border-radius: 4px;
        background-color: #fff;
        color: #333;
        font-size: 14px;
        margin-right: 10px;
    }

    select:hover,
    button:hover {
        background-color: #e6e6e6;
        cursor: pointer;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background-color: #fff;
        border: 1px solid #ccc;
    }

    th,
    td {
        padding: 10px;
        text-align: left;
    }

    th {
        background-color: #f2f2f2;
    }

    tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    a {
        color: #333;
        text-decoration: none;
    }

    a:hover {
        text-decoration: underline;
    }

    .add-button {
        margin-bottom: 10px;
    }

    .view-details-link {
        color: blue;
        text-decoration: underline;
        cursor: pointer;
    }

    .view-details-link:hover {
        color: #0066cc;
    }
    
    .update-button {
        background-color: green;
        color: white;
    }

    .create-button {
        background-color: blue;
        color: white;
    }
</style>
</body>
</html>
