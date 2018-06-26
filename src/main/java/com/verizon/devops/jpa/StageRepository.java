package com.verizon.devops.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface StageRepository extends JpaRepository<Stage, Long> {
    Stage findByName(String name);
	
	@Modifying
    @Transactional
    void deleteByName(String stageeName);
}
