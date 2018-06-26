package com.verizon.devops.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRoleRepository extends JpaRepository<ServiceRole, Long> {
	@Query ("select sr from ServiceRole sr where sr.theRole.name=?1")
	List<ServiceRole> findByRoleName(String roleName);
	
	@Query ("select sr from ServiceRole sr where sr.theService.name=?1 ")
	List<ServiceRole> findByServiceName(String serviceName);
	
	@Query ("select sr from ServiceRole sr where sr.theService.name=?1 and sr.theRole.name=?2 and sr.theEnv.name=?3")
	ServiceRole findByServiceNameAndRoleNameAndStage(String serviceName, String roleName, String stageName);	
	
}
