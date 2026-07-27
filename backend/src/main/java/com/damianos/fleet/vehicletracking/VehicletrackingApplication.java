package com.damianos.fleet.vehicletracking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@EnableScheduling
@SpringBootApplication
public class VehicletrackingApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(VehicletrackingApplication.class, args);





	}


}
