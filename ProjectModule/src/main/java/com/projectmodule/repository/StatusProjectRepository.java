package com.projectmodule.repository;

public interface StatusProjectRepository {
	
	String getStatusFromStatusId(int id);
	
	String getRoleFromRoleId(int id);
	
	int getRoleIdFromRoleName(String roleName);

}
