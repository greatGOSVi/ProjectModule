package com.projectmodule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectmodule.model.Project;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<Project, Integer> {
	
	@Query(value = "SELECT * "
			+ "FROM projects, project_employee "
			+ "WHERE status_project_id = 1 AND projects.project_id = project_employee.project_id AND employee_id = :id",
			nativeQuery = true)
	List<Project> findAllActiveByEmployeeId(@Param("id") Integer id);

}
