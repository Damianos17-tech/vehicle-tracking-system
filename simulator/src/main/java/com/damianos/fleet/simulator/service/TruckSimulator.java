package com.damianos.fleet.simulator.service;

import com.damianos.fleet.simulator.kafka.TruckEventProducer;
import com.damianos.fleet.simulator.model.Position;
import com.damianos.fleet.simulator.model.Truck;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckSimulator {

    private final TruckEventProducer producer;
    private final FleetManager fleetManager;
    private final TruckConditionService truckConditionService;

    private static final int UPDATE_PER_TICK = 5;

    private int index = 0;


    public TruckSimulator(
            TruckEventProducer producer,
            FleetManager fleetManager,
            TruckConditionService truckConditionService
    ) {

        System.out.println("🔥 TruckSimulator CREATED");

        this.producer = producer;
        this.fleetManager = fleetManager;
        this.truckConditionService = truckConditionService;
    }


    @Scheduled(
            fixedRate = 20,
            initialDelay = 3000
    )
    public void tickScheduler() {

        //System.out.println("❤️ TICK ALIVE");

        tick();
    }


    private void tick() {

        List<Truck> trucks = fleetManager.getTrucks();

        if (trucks.isEmpty()) {
            System.out.println("NO TRUCKS YET");
            return;
        }


        int size = trucks.size();


        for (int i = 0; i < UPDATE_PER_TICK; i++) {


            Truck truck = trucks.get(index % size);
            index++;


            Position newPosition = moveTruck(truck);


            if (newPosition == null) {
                continue;
            }


            producer.send(truck);
        }
    }



    private Position moveTruck(Truck truck) {

        Position newPosition = truck.nextPosition();


        if (newPosition != null) {

            truckConditionService.updateAfterDistance(
                    truck,
                    truck.getLastDistanceKm()
            );


            truckConditionService.generateRandomFailures(truck);
        }


        return newPosition;
    }
}