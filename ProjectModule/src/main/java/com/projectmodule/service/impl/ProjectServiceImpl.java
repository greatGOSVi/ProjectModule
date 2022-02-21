package com.projectmodule.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectmodule.dto.EmployeeDto;
import com.projectmodule.dto.MessageDto;
import com.projectmodule.dto.PagedResult;
import com.projectmodule.dto.ProjectDto;
import com.projectmodule.dto.request.ProjectRequest;
import com.projectmodule.dto.request.UpdateProjectRequest;
import com.projectmodule.exception.ElementNotFoundException;
import com.projectmodule.exception.ProjectAlreadyPresentException;
import com.projectmodule.model.Employee;
import com.projectmodule.model.Project;
import com.projectmodule.model.Project_;
import com.projectmodule.repository.EmployeeProjectRepository;
import com.projectmodule.repository.EmployeeRepository;
import com.projectmodule.repository.ProjectRepository;
import com.projectmodule.repository.StatusProjectRepository;
import com.projectmodule.service.ProjectService;
import com.projectmodule.specification.ProjectSpecification;
import com.projectmodule.specification.SearchCriteria;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private StatusProjectRepository statusProjectRepository;
	@Autowired
	private EmployeeProjectRepository employeeProjectRepository;
	@Autowired 
	private EmployeeRepository employeeRepository;
	
	@Override
	@Transactional(readOnly = true)
	public PagedResult<ProjectDto> findAll(String searchValue, Pageable pageable) throws ElementNotFoundException {
		Specification<Project> projectSpecification = Specification.where(null);
		
		if (searchValue != null) {
			Specification<Project> nameSpecification = Specification.where(new ProjectSpecification(new SearchCriteria(Project_.NAME, "~", searchValue)));
			projectSpecification = projectSpecification.or(nameSpecification);
			
		}
		
		Page<Project> pageProjects = projectRepository.findAll(projectSpecification, pageable);
		List<ProjectDto> listProjects = pageProjects.getContent().stream().map(this::convertProjectToDto).collect(Collectors.toList());
		PagedResult<ProjectDto> projectsDto = new PagedResult<>();
		projectsDto.setData(listProjects);
		projectsDto.setPageNumber(pageProjects.getNumber());
		projectsDto.setPageSize(pageProjects.getSize());
		projectsDto.setTotalElements(pageProjects.getTotalElements());
		projectsDto.setTotalPages(pageProjects.getTotalPages());
		
		if (searchValue != null && listProjects.isEmpty()) {
			throw new ElementNotFoundException("No Projects found");
		} else {
			return projectsDto;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProjectDto> findById(int id) throws ElementNotFoundException {
		Optional<Project> optProject = projectRepository.findById(id);
		
		if(optProject.isPresent()) {
			Project p = optProject.get();
			ProjectDto project = convertProjectToDto(p);
			
			return Optional.of(project);
		} else {
			throw new ElementNotFoundException("The Project with the ID " + id + " was not found.");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public PagedResult<ProjectDto> findAllActiveByUsername(String username) throws ElementNotFoundException {
		Optional<Employee> optEmployee = employeeRepository.findByUsername(username);
		
		if(optEmployee.isPresent()) {
			int employeeId = optEmployee.get().getId();
			List<Project> pageProjects = employeeProjectRepository.findAllActiveByEmployeeId(employeeId);
			List<ProjectDto> listProjects = pageProjects.stream().map(this::convertProjectToDto).collect(Collectors.toList());
			PagedResult<ProjectDto> projectsDto = new PagedResult<>();
			projectsDto.setData(listProjects);
			
			if(listProjects.isEmpty()) {
				throw new ElementNotFoundException("No Projects found");
			} else {
				return projectsDto;
			}
		} else {
			throw new ElementNotFoundException("The employee with USERNAME " + username + " can't be found");
		}
	}

	@Override
	public ProjectDto saveProject(ProjectRequest projectRequest) throws ProjectAlreadyPresentException, ElementNotFoundException {
		Optional<Project> optProject = projectRepository.findByName(projectRequest.getName());
		
		if(optProject.isPresent()) {
			throw new ProjectAlreadyPresentException("A Project with name '" + projectRequest.getName() + "' is already present");
		} else {
			Project project = new Project();
			
			if(projectRequest.getStatusId() != 0) {project.setStatusId(projectRequest.getStatusId());} else {project.setStatusId(2);}
			project.setName(projectRequest.getName());
			project.setDescription(projectRequest.getDescription());
			project.setCost(projectRequest.getCost());
			project.setStartDate(Date.valueOf(projectRequest.getStartDate()));
			if(projectRequest.getDuration() != null) {project.setDuration(Date.valueOf(projectRequest.getDuration()));}
			
			Set<Employee> employees = new HashSet<Employee>();
			if(projectRequest.getProjectOwner() != 0) {
				Optional<Employee> projectOwner = employeeRepository.findById(projectRequest.getProjectOwner());
				if (projectOwner.isPresent()) {
					Employee po = projectOwner.get();
					if(po.getRoleId() == 1) {
						employees.add(po);
					} else {
						throw new ElementNotFoundException("Unable to find an employee with ID " + po.getId() + " and PO role");
					}
				} else {
					throw new ElementNotFoundException("The employee with ID " + projectRequest.getProjectOwner() + " can't be found");
				}
			}
			Set<Integer> empIds = projectRequest.getDevelopers();
			for(Integer i : empIds) {
				Optional<Employee> optEmp = employeeRepository.findById(i);
				
				if(optEmp.isPresent()) {
					Employee e = optEmp.get();
					if(e.getRoleId() == 2) {
						employees.add(e);
					} else {
						throw new ElementNotFoundException("Unable to find an employee with ID " + e.getId() + " and DEV role");
					}
				} else {
					throw new ElementNotFoundException("The employee with ID " + i + " can't be found");
				}
			}
			project.setDevelopers(employees);
			
			project = projectRepository.save(project);
			
			return convertProjectToDto(project);
		}
	}
	
	@Override
	public Optional<ProjectDto> updateProject(int id, UpdateProjectRequest projectRequest) throws ElementNotFoundException {
		Optional<Project> optProject = projectRepository.findById(id);
		
		if(optProject.isPresent()) {
			Project project = optProject.get();
			
			if(projectRequest.getStatusId() != 0) {project.setStatusId(projectRequest.getStatusId());}
			if(projectRequest.getName() != null) {project.setName(projectRequest.getName());}
			if(projectRequest.getDescription() != null) {project.setDescription(projectRequest.getDescription());}
			if(projectRequest.getCost() != 0) {project.setCost(projectRequest.getCost());}
			if(projectRequest.getStartDate() != null) {project.setStartDate(Date.valueOf(projectRequest.getStartDate()));}
			if(projectRequest.getDuration() != null) {project.setDuration(Date.valueOf(projectRequest.getDuration()));}
			if(projectRequest.getProjectOwner() != 0) {
				Optional<Employee> optProjectOwner = employeeRepository.findById(projectRequest.getProjectOwner());
				if (optProjectOwner.isPresent()) {
					Employee po = optProjectOwner.get();
					if(po.getRoleId() == 1) {
						Set<Employee> updatedEmployees = new HashSet<Employee>();
						Set<Employee> actualEmployees = project.getDevelopers();
						
						for(Employee e : actualEmployees) {
							if (e.getRoleId() == 2) {updatedEmployees.add(e);}
						}
						updatedEmployees.add(po);
						
						project.setDevelopers(updatedEmployees);
					} else {
						throw new ElementNotFoundException("Unable to find an employee with ID " + po.getId() + " and PO role");
					}
				} else {
					throw new ElementNotFoundException("The employee with ID " + projectRequest.getProjectOwner() + " can't be found");
				}
			}
			if(projectRequest.getRemovedDevelopers() != null) {
				Set<Integer> empIds = projectRequest.getRemovedDevelopers();
				Set<Employee> updatedEmployees = project.getDevelopers();
				
				for(Integer i : empIds) {
					Optional<Employee> optEmployee = employeeRepository.findById(i);
					
					if(optEmployee.isPresent()) {
						Employee e = optEmployee.get();
						
						if(e.getRoleId() == 2) {
							
							if(updatedEmployees.contains(e)) {
								updatedEmployees.remove(e);
							} else {
								throw new ElementNotFoundException("The employee with ID " + i + " can't be found in the project with ID " + project.getId());
							}
						} else {
							throw new ElementNotFoundException("Unable to find an employee with ID " + i + " and DEV role");
						}
					} else {
						throw new ElementNotFoundException("The employee with ID " + i + " can't be found");
					}
				}
				
				project.setDevelopers(updatedEmployees);
			}
			if(projectRequest.getAddedDevelopers() != null) {
				Set<Integer> empIds = projectRequest.getAddedDevelopers();
				Set<Employee> updatedEmployees = project.getDevelopers();
				
				for(Integer i : empIds) {
					Optional<Employee> optEmployee = employeeRepository.findById(i);
					
					if(optEmployee.isPresent()) {
						Employee e = optEmployee.get();
						
						if(e.getRoleId() == 2) {
							updatedEmployees.add(e);
						} else {
							throw new ElementNotFoundException("Unable to find an employee with ID " + e.getId() + " and DEV role");
						}
					} else {
						throw new ElementNotFoundException("The employee with ID " + i + " can't be found");
					}
				}
				
				project.setDevelopers(updatedEmployees);
			}
			
			project = projectRepository.save(project);
			
			return Optional.of(convertProjectToDto(project));
			
		} else {
			throw new ElementNotFoundException("The Project with the ID " + id + " was not found.");
		}
	}
	
	@Override
	public Object closeProject(int id) throws ElementNotFoundException {
		Optional<Project> optProject = projectRepository.findById(id);
		
		if(optProject.isPresent()) {
			Project project = optProject.get();
			project.setStatusId(3);
			project.setDuration(Date.valueOf(LocalDate.now()));
			project = projectRepository.save(project);
			
			return new MessageDto(HttpStatus.OK, "Project closed", new Timestamp(System.currentTimeMillis()));
		} else {
			throw new ElementNotFoundException("The Project with the ID " + id + " was not found.");
		}
	}

	private ProjectDto convertProjectToDto(Project project) {
		ProjectDto projectDto = new ProjectDto();
		String status = statusProjectRepository.getStatusFromStatusId(project.getStatusId());
		
		projectDto.setId(project.getId());
		projectDto.setStatus(status);
		projectDto.setName(project.getName());
		projectDto.setDescription(project.getDescription());
		projectDto.setCost(project.getCost());
		projectDto.setStartDate(project.getStartDate());
		projectDto.setDuration(project.getDuration());
		
		Set<Employee> employees = new HashSet<>();
		employees = project.getDevelopers();
		Set<EmployeeDto> developers = new HashSet<>();
		int poId = statusProjectRepository.getRoleIdFromRoleName("PO");
		for(Employee e : employees) {
			if(poId == e.getRoleId()) {
				projectDto.setProjectOwner(convertEmployeeToDto(e));
			} else {
				developers.add(convertEmployeeToDto(e));
			}
		}
		projectDto.setDevelopers(developers);
		
		return projectDto;
	}
	
	private EmployeeDto convertEmployeeToDto(Employee employee) {
		EmployeeDto employeeDto = new EmployeeDto();
		String role = statusProjectRepository.getRoleFromRoleId(employee.getRoleId());
		
		employeeDto.setId(employee.getId());
		employeeDto.setRole(role);
		
		return employeeDto;
	}
	
}
