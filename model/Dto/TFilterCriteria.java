package com.pennant.prodmtr.model.Dto;

public class TFilterCriteria {
	private Short projectId;
	private String taskStatus;
	private Integer taskSupervisorId;

	public Short getProjectId() {
		return projectId;
	}

	public void setProjectId(Short projectId) {
		this.projectId = projectId;
	}

	public Integer getTaskSupervisorId() {
		return taskSupervisorId;
	}

	public void setTaskSupervisorId(Integer taskSupervisorId) {
		this.taskSupervisorId = taskSupervisorId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}
