package com.projectmodule.repository.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.projectmodule.repository.StatusProjectRepository;

@Repository
public class StatusProjectRepositoryImpl implements StatusProjectRepository {
	
	private NamedParameterJdbcTemplate namedTemplate;
	
	public StatusProjectRepositoryImpl(@Autowired DataSource dataSource) {
		this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public String getStatusFromStatusId(int id) {
		String sId = String.valueOf(id);
		String query = "SELECT (status_name) FROM status_project WHERE " + sId + " = status_id;";
		return (String) namedTemplate.queryForObject(query, new MapSqlParameterSource(), String.class);
	}

	@Override
	public String getRoleFromRoleId(int id) {
		String sId = String.valueOf(id);
		String query = "SELECT (role_name) FROM roles WHERE " + sId + " = role_id;";
		return (String) namedTemplate.queryForObject(query, new MapSqlParameterSource(), String.class);
	}
	
	@Override
	public int getRoleIdFromRoleName(String roleName) {
		String query = "SELECT (role_id) FROM roles WHERE role_name = '" + roleName + "';";
		return (int) namedTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
	}

}
