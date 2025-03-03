package com.fdm.pmstprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fdm.pmstprocessor", "com.fdm.pmscommon"})
@EntityScan(basePackages = {"com.fdm.pmscommon.entities"})
@EnableJpaRepositories(basePackages = {"com.fdm.pmscommon.repositories"})
public class ProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}
}
