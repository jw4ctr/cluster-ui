package com.verizon.devops.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
	
	@Modifying
    @Transactional
    void deleteByName(String roleName);
}
