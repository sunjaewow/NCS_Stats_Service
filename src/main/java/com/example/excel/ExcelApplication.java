package com.example.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExcelApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExcelApplication.class, args);
	}
}
