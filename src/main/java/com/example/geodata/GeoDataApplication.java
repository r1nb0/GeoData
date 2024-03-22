package com.example.geodata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GeoDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoDataApplication.class, args);
	}

}
