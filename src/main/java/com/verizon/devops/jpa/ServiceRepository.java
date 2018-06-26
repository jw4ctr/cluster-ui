package com.verizon.devops.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
	Service findByName(String name);
	
	@Query ("select sr from Service sr where sr.theProject.name=?1")
	List<Service> findByProjectName(String projectName);
	
	@Modifying
    @Transactional
    void deleteByName(String serviceName);
}
