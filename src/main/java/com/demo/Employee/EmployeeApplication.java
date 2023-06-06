package com.demo.Employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(/*exclude = {DataSourceAutoConfiguration.class}*/)
public class EmployeeApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}
}
