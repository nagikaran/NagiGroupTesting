package com.NagiGroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "*") // Allow requests from all origins
@SpringBootApplication
public class NagiGroupDataManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NagiGroupDataManagementApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NagiGroupDataManagementApplication.class);
	}

}
