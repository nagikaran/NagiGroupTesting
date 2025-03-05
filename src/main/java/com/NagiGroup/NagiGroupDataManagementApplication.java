package com.NagiGroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "*") // Allow requests from all origins
@SpringBootApplication
public class NagiGroupDataManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(NagiGroupDataManagementApplication.class, args);
	}

}
