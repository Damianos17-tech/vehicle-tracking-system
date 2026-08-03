package com.damianos.fleet.simulator.service;


import com.damianos.fleet.simulator.model.Position;
import com.damianos.fleet.simulator.model.Truck;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class FleetManager {


    private static final int EXPECTED_TRUCKS = 100;


    private final RouteService routeService;

    private final RestTemplate restTemplate;


    @Getter
    private final List<Truck> trucks = new ArrayList<>();


    @Value("${backend.url}")
    private String backendUrl;


    private final String simulatorId =
            UUID.randomUUID().toString();



    private volatile boolean backendAvailable = false;



    public FleetManager(
            RouteService routeService,
            RestTemplateBuilder builder
    ) {
        this.routeService = routeService;
        this.restTemplate = builder.build();
    }



    public boolean isBackendAvailable() {
        return backendAvailable;
    }




    @PostConstruct
    public void initFleet() {

        Thread.startVirtualThread(() -> {


            while (true) {


                try {


                    registerToBackend();


                    backendAvailable = true;


                    System.out.println(
                            "Simulator registered OK"
                    );


                    break;



                } catch(Exception e) {


                    backendAvailable = false;


                    System.out.println(
                            "Registration failed - retry in 10s"
                    );


                    try {
                        Thread.sleep(10000);
                    }
                    catch(Exception ignored){}

                }

            }


        });

    }






    @Scheduled(fixedDelay = 4000)
    public void sendHeartbeat() {

        try {

            restTemplate.postForLocation(
                    backendUrl
                            + "/fleet/heartbeat?simulatorId="
                            + simulatorId,
                    null
            );


            backendAvailable = true;


        } catch(Exception e){

            backendAvailable = false;

            System.out.println(
                    "HEARTBEAT LOST - STOP PRODUCING"
            );

        }

    }







    private synchronized void registerToBackend(){



        System.out.println(
                "REGISTER SIMULATOR "
                        + simulatorId
        );



        List<Truck> assigned =


                restTemplate.exchange(

                        backendUrl
                                + "/fleet/register?simulatorId="
                                + simulatorId,

                        HttpMethod.GET,

                        null,

                        new ParameterizedTypeReference<List<Truck>>() {}

                ).getBody();




        if(assigned == null || assigned.isEmpty()){


            throw new RuntimeException(
                    "No trucks assigned"
            );

        }




        for(Truck truck : assigned){


            Position destination =
                    new Position();


            truck.setRoute(

                    routeService.generateRoute(
                            truck.getPosition(),
                            destination
                    )

            );

        }




        synchronized(trucks){


            trucks.clear();

            trucks.addAll(assigned);

        }




        System.out.println(
                "Loaded trucks: "
                        + trucks.size()
        );


    }



    public Truck getTruckById(String id) {

        synchronized (trucks) {

            for (Truck truck : trucks) {

                if (truck.getId().equals(id)) {
                    return truck;
                }

            }

        }

        return null;
    }













}