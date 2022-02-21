package com.projectmodule.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.projectmodule.dto.ProjectDto;
import com.projectmodule.dto.request.ProjectRequest;
import com.projectmodule.dto.request.UpdateProjectRequest;
import com.projectmodule.exception.ProjectAlreadyPresentException;
import com.projectmodule.exception.ElementNotFoundException;
import com.projectmodule.dto.PagedResult;

public interface ProjectService {
	
	PagedResult<ProjectDto> findAll(String searchValue, Pageable pageable) throws ElementNotFoundException;
	
	PagedResult<ProjectDto> findAllActiveByEmployeeId(int id) throws ElementNotFoundException;

	Optional<ProjectDto> findById(int id) throws ElementNotFoundException;
	
	ProjectDto saveProject(ProjectRequest projectRequest) throws ProjectAlreadyPresentException, ElementNotFoundException;
	
	Optional<ProjectDto> updateProject(int id, UpdateProjectRequest projectRequest) throws ElementNotFoundException;
	
	Object closeProject(int id) throws ElementNotFoundException;
	
}
