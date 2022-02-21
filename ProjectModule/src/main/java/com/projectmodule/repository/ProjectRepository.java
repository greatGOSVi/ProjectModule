package com.projectmodule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.projectmodule.model.Project;

@Repository
public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer>, JpaSpecificationExecutor<Project> {
	
	Optional<Project> findById(int id);
	
	Optional<Project> findByName(String name);

}
