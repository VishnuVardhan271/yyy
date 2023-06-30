package com.pennant.prodmtr.Dao.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.pennant.prodmtr.Dao.Interface.TaskDao;
import com.pennant.prodmtr.model.Dto.ResTaskFilter;
import com.pennant.prodmtr.model.Dto.TFilterCriteria;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Entity.Project;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

@Repository
@Component
public class TaskDaoImpl implements TaskDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<TaskDto> viewTasksForUser(int userId) {
		return getTasksByUserId(userId);
	}

	public Project findProjectById(Short projectId) {

		return entityManager.find(Project.class, projectId);
	}

	private User findById(int userId) {
		return entityManager.find(User.class, userId);
	}

	public List<TaskDto> getTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT t FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		taskQuery.setParameter("user", user);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	public List<TaskDto> getAllTasks() {
		String jpql = "SELECT t FROM Task t";// query
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	public List<TaskDto> PtfilterTasks(TFilterCriteria tTaskFilter) {
		System.out.println("In filterTasks");

		Short projectId = tTaskFilter.getProjectId();
		String taskStatus = tTaskFilter.getTaskStatus();
		Integer taskSupervisorId = tTaskFilter.getTaskSupervisorId();

		List<TaskDto> filteredTaskDTOs = new ArrayList<>();

		// Construct the query based on the provided filter criteria
		TypedQuery<Task> query = null;

		if (projectId != null && (!taskStatus.isBlank()) && taskSupervisorId != null) {
			// Filter by project, task status, and task supervisor
			query = entityManager.createQuery(
					"SELECT t FROM Task t WHERE t.project.projectId = :projectId AND t.taskStatus = :taskStatus AND t.taskSupervisor.userId = :taskSupervisorId",
					Task.class);
			query.setParameter("projectId", projectId);
			query.setParameter("taskStatus", taskStatus);
			query.setParameter("taskSupervisorId", taskSupervisorId);
		} else if (projectId != null && (!taskStatus.isBlank())) {
			// Filter by project and task status
			query = entityManager.createQuery(
					"SELECT t FROM Task t WHERE t.project.projectId = :projectId AND t.taskStatus = :taskStatus",
					Task.class);
			query.setParameter("projectId", projectId);
			query.setParameter("taskStatus", taskStatus);
		} else if (projectId != null && taskSupervisorId != null) {
			// Filter by project and task supervisor
			query = entityManager.createQuery(
					"SELECT t FROM Task t WHERE t.project.projectId = :projectId AND t.taskSupervisor.userId = :taskSupervisorId",
					Task.class);
			query.setParameter("projectId", projectId);
			query.setParameter("taskSupervisorId", taskSupervisorId);
		} else if ((!taskStatus.isBlank()) && taskSupervisorId != null) {
			// Filter by task status and task supervisor
			query = entityManager.createQuery(
					"SELECT t FROM Task t WHERE t.taskStatus = :taskStatus AND t.taskSupervisor.userId = :taskSupervisorId",
					Task.class);
			query.setParameter("taskStatus", taskStatus);
			query.setParameter("taskSupervisorId", taskSupervisorId);
		} else if (projectId != null) {
			// Filter by project only
			query = entityManager.createQuery("SELECT t FROM Task t WHERE t.project.projectId = :projectId",
					Task.class);
			query.setParameter("projectId", projectId);
		} else if (!taskStatus.isBlank()) {
			// Filter by task status only
			query = entityManager.createQuery("SELECT t FROM Task t WHERE t.taskStatus = :taskStatus", Task.class);
			query.setParameter("taskStatus", taskStatus);
		} else if (taskSupervisorId != null) {
			// Filter by task supervisor only
			query = entityManager.createQuery("SELECT t FROM Task t WHERE t.taskSupervisor.userId = :taskSupervisorId",
					Task.class);
			query.setParameter("taskSupervisorId", taskSupervisorId);
		} else {
			// No filtering criteria provided, retrieve all tasks
			query = entityManager.createQuery("SELECT t FROM Task t", Task.class);
		}

		List<Task> filteredTasks = query.getResultList();

		// Convert the filtered tasks to TaskDto objects
		for (Task task : filteredTasks) {
			TaskDto taskDto = TaskDto.fromEntity(task);
			filteredTaskDTOs.add(taskDto);
		}

		return filteredTaskDTOs;
	}

	public Task getTaskById(int taskId) {
		return entityManager.find(Task.class, taskId);
	}

	public void saveTask(Task task) {
		entityManager.merge(task);
	}

	public boolean updateStatus(int taskId) {
		String jpql = "UPDATE Task t SET t.taskStatus = :status WHERE t.taskId = :taskId";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("status", "rew");
		query.setParameter("taskId", taskId);

		int rowsAffected = query.executeUpdate();
		return rowsAffected > 0;
	}

	public List<Task> getTasksByProjectId(int projId) {
		// TODO Auto-generated method stub
		short projectId = (short) projId;
		TypedQuery<Task> query = entityManager
				.createQuery("SELECT pt FROM Task pt WHERE pt.project.projectId = :projectId", Task.class);
		query.setParameter("projectId", projectId);
		return query.getResultList();
	}

	public List<Task> getCompTasksByProjectId(int projId) {
		// TODO Auto-generated method stub
		short projectId = (short) projId;
		TypedQuery<Task> query = entityManager.createQuery(
				"SELECT pt FROM Task pt WHERE pt.project.projectId = :projectId and pt.taskStatus = 'DONE' ",
				Task.class);
		query.setParameter("projectId", projectId);
		return query.getResultList();
	}

	public int getCompletedTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user AND t.taskStatus = 'completed'";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	public int getTotalTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	public double getHoursWorkedByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT SUM(t.numberOfHoursRequired) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
		query.setParameter("user", user);
		BigDecimal result = query.getSingleResult();
		return result != null ? result.doubleValue() : 0;
	}

	@Override
	public void updateEntity(int taskId, String remarks, String status) {

		Task task = entityManager.find(Task.class, taskId);
		System.out.println("entity " + task);
		task.setTaskRemarks(remarks);
		task.setTaskStatus(status);
		entityManager.merge(task);
		entityManager.flush();
	}

	@Override
	public void setTaskStatus(int taskId, String taskRemarks, String taskStatus) {
		Task task = entityManager.find(Task.class, taskId);
		task.setTaskRemarks(taskRemarks);
		task.setTaskStatus(taskStatus);
		entityManager.persist(task);
	}

	@Override
	public List<TaskDto> filterTasks(ResTaskFilter resTaskFilter) {
		System.out.println("In filterTasks");

		String jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId";

		TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);

		query.setParameter("userId", resTaskFilter.getUserId());

		if (resTaskFilter.getProject() != null && !resTaskFilter.getStatus().isEmpty()
				&& !resTaskFilter.getCategory().isEmpty()) {
			jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId AND t.taskCategory = :category AND t.taskStatus = :status AND t.project.id = :projectId";
			findProjectById(resTaskFilter.getProject());
			query.setParameter("projectId", resTaskFilter.getProject());
		} else {
			if (resTaskFilter.getProject() != null && resTaskFilter.getProject() != 0) {
				findProjectById(resTaskFilter.getProject());
				query.setParameter("projectId", resTaskFilter.getProject());
			}

			if (resTaskFilter.getStatus() != null && !resTaskFilter.getStatus().isEmpty()) {
				query.setParameter("status", resTaskFilter.getStatus());
			}

			if (resTaskFilter.getCategory() != null && !resTaskFilter.getCategory().isEmpty()) {
				query.setParameter("category", resTaskFilter.getCategory());
			}
		}

		List<Task> filteredTasks = query.getResultList();
		List<TaskDto> filteredTaskDTOs = new ArrayList<>();

		for (Task task : filteredTasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			filteredTaskDTOs.add(dto);
		}

		return filteredTaskDTOs;
	}
}