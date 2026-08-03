package com.damianos.fleet.vehicletracking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@EnableScheduling
@SpringBootApplication
@EnableKafka
public class VehicletrackingApplication {

	public static void main(String[] args)
	{



		System.out.println("DB URL = " + System.getenv("DB_URL"));
		SpringApplication.run(VehicletrackingApplication.class, args);





	}


}
