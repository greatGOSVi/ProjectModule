package com.projectmodule.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.projectmodule.model.Project;

public class ProjectSpecification implements Specification<Project> {

	private static final long serialVersionUID = 1L;
	
	private SearchCriteria criteria;
	
	public ProjectSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		
		if (criteria.getOperation().equals("~")) {
			return criteriaBuilder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
		} else if (criteria.getOperation().equals("^")) {
			return criteriaBuilder.like(root.join("employees").get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
		}
		
		return null;
	}
	
}
