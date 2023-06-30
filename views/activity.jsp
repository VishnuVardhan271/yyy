<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <title>Activity Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 20px;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2f2f2;
            color: #333;
            font-weight: bold;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        .clickable {
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h1>Activity Tasks</h1>

    <table>
        <tr class="clickable" onclick="onTaskRowClick('${task.taskId}')">
            <th>Task ID</th>
            <th>Task Name</th>
            <th>Task Category</th>
            <th>Task Status</th>
            <!-- Add more table headers as needed -->
        </tr>

        <c:forEach items="${activityTasks}" var="task">
            <tr id="taskRow-${task.taskId}" class="clickable" onclick="onTaskRowClick('${task.taskId}')">
                <td>${task.taskId}</td>
                <td>${task.taskName}</td>
                <td>${task.taskCategory}</td>
                <td>${task.taskStatus}</td>
                <!-- Display more task properties in respective table cells -->
            </tr>
        </c:forEach>
    </table>
    <br><br>
    <div>
        <h1>Activity Subtasks</h1>
        <table>
            <tr>
                <th>Task ID</th>
                <th>Subtask ID</th>
                <th>Subtask Description</th>
                <th>Number of Hours</th>
                <th>Creation Date</th>
                <th>Status</th>
                <!-- Add more table headers as needed -->
            </tr>

            <c:forEach items="${activitySubTasks}" var="subtask">
                <tr id="subtaskRow-${subtask.primaryKey.subtaskId}" class="clickable" onclick="onSubtaskRowClick('${subtask.primaryKey.taskId}', '${subtask.primaryKey.subtaskId}')">
                    <td>${subtask.primaryKey.taskId}</td>
                    <td>${subtask.primaryKey.subtaskId}</td>
                    <td>${subtask.subtaskDescription}</td>
                    <td>${subtask.numberOfHours}</td>
                    <td>${subtask.creationDate}</td>
                    <td>${subtask.sbts_status}</td>
                    <!-- Display more subtask properties in respective table cells -->
                </tr>
            </c:forEach>
        </table>
    </div>

    <script>
    function onSubtaskRowClick(taskId, subtaskId) {
        var paramValue = taskId + "," + subtaskId;
        window.location.href = "setSubTaskStatus?compostiteId=" + paramValue;
    }
    // JavaScript function to handle row clicks for tasks
    function onTaskRowClick(taskId) {
    	
    	console.log("here");
        window.location.href = "setTaskStatus?taskId=" + taskId;
    }

    // JavaScript function to handle row clicks for subtasks

    </script>
</body>
</html>