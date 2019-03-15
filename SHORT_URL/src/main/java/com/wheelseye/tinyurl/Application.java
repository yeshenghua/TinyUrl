package com.wheelseye.tinyurl;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//Task 1: It should be a spring boot project

@SpringBootApplication
@EnableCaching
public class Application {
	
	public static void main(String args[])
	{
		SpringApplication.run(Application.class, args);
	}

}

