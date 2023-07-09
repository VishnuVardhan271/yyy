package com.pennant.prodmtr.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.TFilterCriteria;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.model.view.TaskUpdateFormModel;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class TaskController {

	private final TaskService taskService;
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	// method for view the tasks for user
	@RequestMapping(value = "/tasksbyid", method = RequestMethod.GET)
	public String viewTasksForUser(Model model, HttpSession session) {
		try {
			// Check if the user is logged in
			User user = (User) session.getAttribute("user");
			if (user == null) {
				// User is not logged in, redirect to the login page
				return "redirect:/login";
			}

			// User is logged in, fetch the tasks
			List<TaskDto> tasks = taskService.getTasksByUserId(user.getUserId());

			// Add the tasks to the model
			model.addAttribute("tasks", tasks);
			logger.info("Tasks loaded by user");
			// Return the view name
			String userTask = "User";
			model.addAttribute("Task", userTask);
			return "Taskslist";
		} catch (Exception e) {
			logger.error("Error occurred while loading tasks for user", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while loading tasks.");
			return "errorPage";
		}
	}

	// method for get all the tasks
	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public String viewAllTasks(Model model) {
		try {
			List<TaskDto> tasks = taskService.getAllTasks();

			// Add the tasks to the model
			model.addAttribute("tasks", tasks);
			logger.info("Viewing all tasks");
			String userTask = "Task";
			model.addAttribute("Task", userTask);
			// Return the view name
			return "Taskslist";
		} catch (Exception e) {
			logger.error("Error occurred while loading all tasks", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while loading tasks.");
			return "errorPage";
		}
	}

	// method for get the task details by id
	@RequestMapping(value = "/taskdetailsbyid", method = RequestMethod.GET)
	public String getAllTasks(Model model) {
		try {
			List<TaskDto> tasks = taskService.getAllTasks();

			model.addAttribute("tasks", tasks);
			logger.info("Viewing task details by ID");
			return "tasksdetailsbyid";
		} catch (Exception e) {
			logger.error("Error occurred while loading task details", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while loading task details.");
			return "errorPage";
		}
	}

	// method for update status
	@RequestMapping(value = "/updateTaskStatus", method = RequestMethod.GET)
	public String updateTaskStatus(@RequestParam("taskId") int taskId, Model model) {
		try {
			// Retrieve the existing task from the database using the task ID
			Task task = taskService.getTaskById(taskId);
			model.addAttribute("task", task);
			logger.info("Updating task status");

			return "taskStatusUpdate";
		} catch (Exception e) {
			logger.error("Error occurred while updating task status", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while updating task status.");
			return "errorPage";
		}
	}

	// method for update success
	@RequestMapping(value = "/updateSuccess", method = RequestMethod.POST)
	public String updateTaskStatusSuccess(@RequestParam("taskId") int taskId) {
		try {
			// Retrieve the existing task from the database using the task ID
			Boolean task = taskService.updateStatus(taskId);

			// Update the task status

			// Redirect to the task list page
			return "redirect:/tasks";
		} catch (Exception e) {
			logger.error("Error occurred while updating task status", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while updating task status.");
			return "errorPage";
		}
	}

	@RequestMapping(value = "/Taskfilter", method = RequestMethod.POST)
	@ResponseBody
	public String filterTasks(@Validated TFilterCriteria filterCriteria) {
		try {
			List<TaskDto> filteredTasks = taskService.TfilterTasks(filterCriteria);
			System.out.println(filteredTasks);

			Gson gson = new Gson();
			String json = gson.toJson(filteredTasks);
			logger.info("Filtered tasks");

			return json;
		} catch (Exception e) {
			logger.error("Error occurred while filtering tasks", e);
			// Handle the exception accordingly (e.g., return an error JSON)
			return "{\"error\": \"An error occurred while filtering tasks.\"}";
		}
	}

	// method for get individual tasks
	@RequestMapping(value = "/Indvtasks", method = RequestMethod.GET)
	public String viewIndvtasks(@RequestParam("projId") Integer projId, Model model) {
		try {
			// Fetch tasks based on the provided project ID
			List<Task> tasks = taskService.getTasksByProjectId(projId);

			// Add the tasks to the model
			model.addAttribute("tasks", tasks);
			logger.info("Individual tasks loaded");

			return "Indvtasks"; // Replace with actual view name
		} catch (Exception e) {
			logger.error("Error occurred while loading individual tasks", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while loading individual tasks.");
			return "errorPage";
		}
	}

	// method for set task status
	@RequestMapping(value = "/setTaskStatus", method = RequestMethod.GET)
	public String setTaskStatus(@RequestParam int taskId, Model model, HttpSession session) {
		try {
			// Fetch the task based on the provided task ID
			Task task = taskService.getTaskById(taskId);
			model.addAttribute("task", task);
			logger.info("Setting task status");
			return "taskStatusUpdate";
		} catch (Exception e) {
			logger.error("Error occurred while setting task status", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while setting task status.");
			return "errorPage";
		}
	}

	// methodfor set task details
	@RequestMapping(value = "/setTaskDetails", method = RequestMethod.GET)
	public String setTaskUpdateFormModel(@Validated TaskUpdateFormModel taskUpdateFormModel, Model model) {
		try {
			// Update the task status based on the provided form model
			taskService.updateTaskStatus(taskUpdateFormModel);
			return "redirect:activity";
		} catch (Exception e) {
			logger.error("Error occurred while setting task details", e);
			// Add error message to the model
			model.addAttribute("error", "An error occurred while setting task details.");
			return "errorPage";
		}
	}
}
