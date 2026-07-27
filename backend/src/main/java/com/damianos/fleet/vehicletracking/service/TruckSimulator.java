package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.Position;
import com.damianos.fleet.vehicletracking.model.Truck;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TruckSimulator {

    private final TruckPositionPublisher publisher;
    private final FleetManager fleetManager;
    private final TruckConditionService truckConditionService;


    public TruckSimulator(TruckPositionPublisher publisher, FleetManager fleetManager, TruckConditionService truckConditionService) {
        this.publisher = publisher;
        this.fleetManager = fleetManager;
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

        List<Truck> trucks = fleetManager.getTrucks();
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



























    private void tickOld() {
        //System.out.println("TICK >>> simulator running");

        fleetManager.getTrucks().forEach(truck -> {

            Position newPos = moveTruck(truck);
            if (newPos == null) { return; }

            //System.out.println(truck.getId() + " -> " + newPos.getLatitude() + ", " + newPos.getLongitude());
           /* System.out.println(
                    truck.getId()
                            + " | pos=" + newPos
                            + " | speed=" + truck.getSpeed()
                            + " km/h"
                            + " | status=" + truck.getStatus()
                            + " | warnings=" + truck.getWarnings()
                            + " | failures=" + truck.getFailures()
                            + " | fuel=" + String.format("%.2f", truck.getFuelLevel())
                            + "%"
                            + " | condition=" + String.format("%.2f", truck.getTechnicalCondition())
                            + "%"
                            + " | przebieg: km=" + String.format("%.3f", truck.getTotalDistanceKm())
                            + " | events=" + truck.getEventsText()
                            + " | next service =" + truck.getKmToService()
            );*/

            publisher.send(truck);
        });
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