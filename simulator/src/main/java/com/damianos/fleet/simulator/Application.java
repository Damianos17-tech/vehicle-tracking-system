package com.damianos.fleet.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {


		ConfigurableApplicationContext context =
				SpringApplication.run(Application.class, args);


		System.out.println("=================================");
		System.out.println("SIMULATOR STARTED");

		System.out.println(
				"KAFKA GROUP: "
						+ context.getEnvironment()
						.getProperty("spring.kafka.consumer.group-id")
		);

		System.out.println(
				"KAFKA SERVER: "
						+ context.getEnvironment()
						.getProperty("spring.kafka.bootstrap-servers")
		);

		System.out.println("=================================");





		SpringApplication.run(Application.class, args);
	}



}