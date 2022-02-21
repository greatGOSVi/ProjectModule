package com.projectmodule.controller;

import java.sql.Timestamp;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;

import com.projectmodule.dto.request.ProjectRequest;
import com.projectmodule.dto.request.UpdateProjectRequest;
import com.projectmodule.exception.ProjectAlreadyPresentException;
import com.projectmodule.exception.ElementNotFoundException;
import com.projectmodule.dto.ErrorDto;
import com.projectmodule.dto.MessageDto;
import com.projectmodule.dto.ProjectDto;
import com.projectmodule.service.ProjectService;
import com.projectmodule.util.ErrorUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping
	@Operation(summary = "Endpoint that retrieves all Projects. Can be paged, sorted and filtered.",
		parameters = {
			@Parameter(name = "page", in = ParameterIn.QUERY, required = false, description = "Result pages you want to retrieve (0..N)."),
			@Parameter(name = "size", in = ParameterIn.QUERY, required = false, description = "Number of records per page."),
			@Parameter(name = "sort", in = ParameterIn.QUERY, required = false, description = "Sorting criteria in the format: 'property(,asc|desc)'. "
					+ "Default sort order is ascending. "
					+ "Multiple sort criteria is allowed.")
		},
		responses = {
			@ApiResponse(description = "Projects Found", responseCode = "200", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ProjectDto.class))),
			@ApiResponse(description = "No Projects Found", responseCode = "404", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorDto.class)))})
	public ResponseEntity<Object> findAll(@RequestParam(name = "search", required = false) String searchValue, Pageable pageable) {
		try {
			return new ResponseEntity<Object>(projectService.findAll(searchValue, pageable), HttpStatus.OK);
		} catch(ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Endpoint that retrieves a Project by its Id.",
		responses = {
			@ApiResponse(description = "Project Found", responseCode = "200", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ProjectDto.class))),
			@ApiResponse(description = "No Project Found", responseCode = "404", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorDto.class)))})
	public ResponseEntity<Object> findById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Object>(projectService.findById(id), HttpStatus.OK);
		} catch(ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/my_projects/{username}")
	public ResponseEntity<Object> findAllActiveByEmployeeId(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<Object>(projectService.findAllActiveByUsername(username), HttpStatus.OK);
		} catch(ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	@Operation(summary = "Endpoint that posts a Project.",
	responses = {
		@ApiResponse(description = "Project Created", responseCode = "201", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ProjectDto.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorDto.class)))})
	public ResponseEntity<Object> addProject(@RequestBody @Valid ProjectRequest projectRequest, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(
					new ErrorDto(HttpStatus.BAD_REQUEST, ErrorUtils.getErrorMessage(result), new Timestamp(System.currentTimeMillis())), HttpStatus.BAD_REQUEST);
		}
		
		try {
			return new ResponseEntity<Object>(projectService.saveProject(projectRequest), HttpStatus.CREATED);
		} catch (ProjectAlreadyPresentException | ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Endpoint that updates a Project by Id.",
	responses = {
		@ApiResponse(description = "Project Updated", responseCode = "200", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ProjectDto.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorDto.class))),
		@ApiResponse(description = "No Project Found", responseCode = "404", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorDto.class)))})
	public ResponseEntity<Object> updateProject(@PathVariable("id") int id, @RequestBody @Valid UpdateProjectRequest projectRequest, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(
					new ErrorDto(HttpStatus.BAD_REQUEST, ErrorUtils.getErrorMessage(result), new Timestamp(System.currentTimeMillis())), HttpStatus.BAD_REQUEST);
		}
		
		try {
			return new ResponseEntity<Object>(projectService.updateProject(id, projectRequest), HttpStatus.OK);
		} catch (ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.NOT_FOUND);
		}
	}
	
	@PatchMapping("/close/{id}")
	@Operation(summary = "Endpoint that closes a Project by Id.",
	responses = {
		@ApiResponse(description = "Project Closed", responseCode = "200", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = MessageDto.class))),
		@ApiResponse(description = "No Project Found", responseCode = "404", content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorDto.class)))})
	public ResponseEntity<Object> closeProject(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Object>(projectService.closeProject(id), HttpStatus.OK);
		} catch (ElementNotFoundException e) {
			return new ResponseEntity<Object>(
					new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), new Timestamp(System.currentTimeMillis())), HttpStatus.NOT_FOUND);
		}
	}
	
}
