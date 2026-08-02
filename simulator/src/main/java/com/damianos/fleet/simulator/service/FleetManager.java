package com.damianos.fleet.simulator.service;


import com.damianos.fleet.simulator.model.Position;
import com.damianos.fleet.simulator.model.Truck;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    private volatile boolean alreadyRegistered = false;




    public FleetManager(
            RouteService routeService,
            RestTemplateBuilder builder
    ) {
        this.routeService = routeService;
        this.restTemplate = builder.build();
    }





    @PostConstruct
    public void initFleet() {


        Thread.startVirtualThread(() -> {


            while(!alreadyRegistered) {


                try {


                    synchronized (trucks) {


                        if(trucks.size() >= EXPECTED_TRUCKS) {


                            System.out.println(
                                    "Simulator already initialized: "
                                            + trucks.size()
                                            + " trucks"
                            );


                            backendAvailable = true;

                            break;
                        }

                    }




                    registerToBackend();
                    alreadyRegistered = true;


                    backendAvailable = true;


                    System.out.println(
                            "Backend connection OK"
                    );


                    break;



                } catch(Exception e) {


                    backendAvailable = false;
                    alreadyRegistered = false;


                    System.out.println(
                            "Backend unavailable - retry in 10s"
                    );



                    try {

                        Thread.sleep(10000);

                    } catch(Exception ignored) {}

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



            if(!backendAvailable) {


                System.out.println(
                        "Backend restored"
                );


                synchronized (trucks) {


                    if(trucks.size() >= EXPECTED_TRUCKS) {


                        System.out.println(
                                "Existing trucks reused"
                        );


                    } else {


                        System.out.println(
                                "Reloading trucks"
                        );


                        registerToBackend();

                    }


                }



                backendAvailable = true;

            }




        } catch(Exception e) {


            backendAvailable = false;



            synchronized (trucks) {

                trucks.clear();

            }



            System.out.println(
                    "Backend unavailable - trucks cleared"
            );

        }

    }








    private void registerToBackend() {



        System.out.println(
                "Registering simulator: "
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





        if(assigned == null || assigned.isEmpty()) {


            throw new RuntimeException(
                    "No trucks received"
            );

        }







        assigned.forEach(truck -> {


            Position destination =
                    new Position();



            truck.setRoute(

                    routeService.generateRoute(

                            truck.getPosition(),

                            destination

                    )

            );


        });







        synchronized (trucks) {


            trucks.clear();


            trucks.addAll(assigned);


        }





        System.out.println(

                "Simulator loaded trucks: "
                        + trucks.size()

        );


    }








    public void addTruck(Truck truck) {


        synchronized (trucks) {

            trucks.add(truck);

        }

    }







    public Truck getTruckById(String id) {


        synchronized (trucks) {


            return trucks.stream()

                    .filter(truck ->
                            truck.getId().equals(id)
                    )

                    .findFirst()

                    .orElse(null);

        }

    }







    public void removeTruck(String id) {


        synchronized (trucks) {


            trucks.removeIf(

                    truck ->
                            truck.getId().equals(id)

            );

        }

    }



}