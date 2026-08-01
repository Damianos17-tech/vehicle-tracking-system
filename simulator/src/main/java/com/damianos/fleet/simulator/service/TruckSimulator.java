package com.damianos.fleet.simulator.service;

import com.damianos.fleet.simulator.kafka.TruckEventProducer;
import com.damianos.fleet.simulator.model.Position;
import com.damianos.fleet.simulator.model.Truck;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TruckSimulator {

    private final TruckEventProducer producer;
    private final FleetManager fleetManager;
    private final TruckConditionService truckConditionService;


    public TruckSimulator(TruckEventProducer producer, FleetManager fleetManager, TruckConditionService truckConditionService) {
        this.producer = producer;
        this.fleetManager = fleetManager;
        this.truckConditionService = truckConditionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::tick, 3000, 1000, TimeUnit.MILLISECONDS);
    }




    private static final int UPDATE_PER_TICK = 3; // limit wysyłki
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


            producer.send(truck);
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