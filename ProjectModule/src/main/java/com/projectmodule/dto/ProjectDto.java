package com.projectmodule.dto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class ProjectDto {
	
	private int id;
	
	private String status;
	
	private String name;
	
	private String description;
	
	private double cost;
	
	private Date startDate;
	
	private Date duration;
	
	private EmployeeDto projectOwner;
	
	private Set<EmployeeDto> developers = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	public EmployeeDto getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(EmployeeDto projectOwner) {
		this.projectOwner = projectOwner;
	}

	public Set<EmployeeDto> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<EmployeeDto> developers) {
		this.developers = developers;
	}

}
