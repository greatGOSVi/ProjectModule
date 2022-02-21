package edu.itk.project.security.dto;

import java.util.HashSet;
import java.util.Set;

public class PatchProjectRequest {

	private int statusId;

	private String name;
	
	private String description;
	
	private double cost;
	
	private String startDate;
	
	private String duration;
	
	private int projectOwner;
		
	private Set<Integer> addedDevelopers = new HashSet<>();
	
	private Set<Integer> removedDevelopers = new HashSet<>();
	
	public PatchProjectRequest() {}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(int projectOwner) {
		this.projectOwner = projectOwner;
	}

	public Set<Integer> getAddedDevelopers() {
		return addedDevelopers;
	}

	public void setAddedDevelopers(Set<Integer> addedDevelopers) {
		this.addedDevelopers = addedDevelopers;
	}

	public Set<Integer> getRemovedDevelopers() {
		return removedDevelopers;
	}

	public void setRemovedDevelopers(Set<Integer> removedDevelopers) {
		this.removedDevelopers = removedDevelopers;
	}

}
