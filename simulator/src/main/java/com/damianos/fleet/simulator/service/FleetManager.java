package com.damianos.fleet.simulator.service;



import com.damianos.fleet.simulator.model.OsrmResponse;
import com.damianos.fleet.simulator.model.Position;
import com.damianos.fleet.simulator.model.Truck;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.damianos.fleet.simulator.model.TruckState.*;

@Component
@Service
public class FleetManager {

    private final RouteService routeService;

    @Getter
    private final List<Truck> trucks = new ArrayList<>();

    public FleetManager(RouteService routeService){
        this.routeService = routeService;
    }

    public void addTruck(Truck truck) {
        trucks.add(truck);
    }


    public Truck getTruckById(String id) {

        return trucks.stream()
                .filter(truck -> truck.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public void removeTruck(String id) {

        trucks.removeIf(
                truck -> truck.getId().equals(id)
        );
    }




    @PostConstruct
    public void initFleet() {


        trucks.add(new Truck(
                "TRUCK-001",
                "DAF XF 460 tandem",
                90,
                Status.ACTIVE
        ));


        trucks.add(new Truck(
                "TRUCK-002",
                "DAF XF 530 plandeka",
                83,
                Status.ACTIVE
        ));


        trucks.add(new Truck(
                "TRUCK-004",
                "Mercedes Actros 1851 cysterna",
                80,
                Status.ACTIVE
        ));

        // itd...


        for (int i = 3; i <= 77; i++) {

            trucks.add(
                    new Truck(
                            "TRUCK-" + i,
                            "Truck " + i,
                            90,
                            Status.ACTIVE
                    )
            );
        }


        trucks.forEach(truck -> {

            Position destination = new Position();

            truck.setRoute(
                    routeService.generateRoute(
                            truck.getPosition(),
                            destination
                    )
            );

        });


        System.out.println(
                "Fleet initialized: "
                        + trucks.size()
                        + " trucks"
        );
    }













//    @PostConstruct
//    public void initFleet() {
//
//        trucks.add(new Truck(
//                "TRUCK-001",
//                "MAN TGX",
//                90,
//                Status.ACTIVE
//        ));
//
//        trucks.add(new Truck(
//                "TRUCK-002",
//                "Volvo FH16",
//                90,
//                Status.ACTIVE
//        ));
//
//        trucks.add(new Truck(
//                "TRUCK-003",
//                "Scania R500",
//                90,
//                Status.ACTIVE
//        ));
//
//        for(Truck truck : trucks){
//            //truck.setPosition(new Position());
//
//            Position start = truck.getPosition();
//            Position destination = new Position();
//            truck.setRoute(routeService.generateRoute(start, destination));
//
//
//            System.out.println(
//                    truck.getId() +
//                            " route = " +
//                            truck.getRoute()
//            );
//        }
//
//
//        //generateNewRoute
//
//        System.out.println("Fleet initialized: " + trucks.size() + " trucks");
//    }
}