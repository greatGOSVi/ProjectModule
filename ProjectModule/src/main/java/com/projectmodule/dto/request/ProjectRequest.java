package com.projectmodule.dto.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProjectRequest {
	
	private final String DATEREGEXP = "^\\d{4}-(02-(0[1-9]|[12][0-9])|(0[469]|11)-(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))$";
	
	@Min(value = 0, message = "must be 1 for active or 2 for unactive")
	@Max(value = 2, message = "must be 1 for active or 2 for unactive")
	private int statusId;
	
	@NotNull
	@Size(min = 1, max = 40, message = "size must be between 1 and 40")
	private String name;
	
	@NotNull
	@Size(min = 4, max = 155, message = "size must be between 4 and 155")
	private String description;
	
	@Min(value = 0, message = "must be greater than or equal to 0")
	private double cost;
	
	@NotNull
	@Pattern(regexp = DATEREGEXP, message = "must be in 'yyyy-MM-dd' format")
	private String startDate;
	
	@Pattern(regexp = DATEREGEXP, message = "must be in 'yyyy-MM-dd' format")
	private String duration;
	
	private int projectOwner;
	
	@NotEmpty
	private Set<Integer> developers = new HashSet<>();

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

	public Set<Integer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<Integer> developers) {
		this.developers = developers;
	}

}
