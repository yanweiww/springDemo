package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
@PropertySource(value = "classpath:my.properties",encoding = "utf-8")
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

		System.out.println("GGGGGGGGGGGGGGGGGGGGGGGG");
	}
}
