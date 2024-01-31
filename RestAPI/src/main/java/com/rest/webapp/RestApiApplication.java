package com.rest.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.rest.webapp.controller.ParsingWebappController;

@SpringBootApplication
@ComponentScan({"com.rest.webapp","com.strabag.app"})
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

}
