package com.rsosor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RsosoRApplication {

	public static void main(String[] args) {
		// Customize the spring config location
		System.setProperty("spring.config.additional-location",
				"optional:file:${user.home}/.RsosoR/,optional:file:${user.home}/RsosoR-dev/");

		// Run application
		SpringApplication.run(RsosoRApplication.class, args);
	}

}
