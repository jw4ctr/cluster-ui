package com.verizon.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClusterUIApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClusterUIApplication.class, args);
	}

}
