package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.config.FleetAllocationService;
import com.damianos.fleet.vehicletracking.model.Position;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@Service
public class TruckSimulator {

    private final TruckPositionPublisher publisher;
    //private final FleetManager fleetManager;
    private final TruckConditionService truckConditionService;

    private final FleetAllocationService fleetAllocationService;


    public TruckSimulator(TruckPositionPublisher publisher, TruckConditionService truckConditionService, FleetAllocationService fleetAllocationService) {
        this.publisher = publisher;

        this.fleetAllocationService = fleetAllocationService;

        this.truckConditionService = truckConditionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::tick, 3000, 20, TimeUnit.MILLISECONDS);
    }




    private static final int UPDATE_PER_TICK = 5; // limit wysyłki
    private int index = 0;

    private void tick() {

        //List<Truck> trucks = fleetManager.getTrucks();
        List<Truck> trucks = fleetAllocationService.getOnlineTrucks();


        if (trucks.isEmpty()) {
            System.out.println("NO TRUCKS YET");
            return;
        }
        int size = trucks.size();

        for (int i = 0; i < UPDATE_PER_TICK; i++) {

            Truck truck = trucks.get(index % size);
            index++;

            Position newPos = moveTruck(truck);


            if (newPos == null) continue;


            publisher.send(truck);
        }
    }


























    private Position moveTruck(Truck truck) {
        Position newPosition = truck.nextPosition();

        if (newPosition != null) {
            truckConditionService.updateAfterDistance(truck, truck.getLastDistanceKm());
            truckConditionService.generateRandomFailures(truck);
        }

        return newPosition;
    }
}