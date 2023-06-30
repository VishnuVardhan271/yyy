<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pennant.prodmtr.model.Entity.Sprint" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sprint Details</title>
    <style>
        .sprint-details-container {
            width: 400px;
            margin: 20px auto;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 20px;
        }
        
        .sprint-details-header {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .sprint-details-info {
            font-size: 16px;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <div class="sprint-details-container">
        <div class="sprint-details-header">
            Sprint Details
        </div>
        
        <table>
		    <tr>
		        <th>Sprint ID</th>
		        <th>Module ID</th>
		        <th>Master ID</th>
		        <th>Start Date</th>
		        <th>End Date</th>
		        <th>Project ID</th>
		        <th>Sprint Name</th>
		    </tr>
		    <c:forEach items="${sprintsByProjId}" var="sprint">
		        <tr>
		            <td>${sprint.sprintId}</td>
		            <td>${sprint.moduleId}</td>
		            <td>${sprint.masterId}</td>
		            <td>${sprint.startDate}</td>
		            <td>${sprint.endDate}</td>
		            <td>${sprint.projectId.projectId}</td>
		            <td>${sprint.sprintName}</td>
		        </tr>
		    </c:forEach>
		</table>
    </div>
</body>
</html>