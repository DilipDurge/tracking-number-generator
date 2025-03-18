package com.tracking.number.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrackingNumberGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingNumberGeneratorApplication.class, args);
		System.out.println("Number Tracking generator Started");
	}

}
