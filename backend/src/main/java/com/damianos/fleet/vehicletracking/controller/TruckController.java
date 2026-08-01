package com.damianos.fleet.vehicletracking.controller;

import com.damianos.fleet.vehicletracking.kafka.TruckCommandProducer;
import com.damianos.fleet.vehicletracking.model.FleetStats;
import com.damianos.fleet.vehicletracking.model.InfrastructureStats;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.service.FleetManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/fleet")
public class TruckController {


    private final FleetManager fleetManager;
    private final TruckCommandProducer commandProducer;


    public TruckController(
            FleetManager fleetManager,
            TruckCommandProducer commandProducer
    ) {
        this.fleetManager = fleetManager;
        this.commandProducer = commandProducer;
    }



    @GetMapping
    public List<Truck> getTrucks() {
        return fleetManager.getTrucks();
    }



    @PostMapping("/truck/{id}/repair")
    public void repairTruck(
            @PathVariable String id
    ) {

        commandProducer.sendCommand(
                id,
                "REPAIR"
        );
    }



    @PostMapping("/truck/{id}/service")
    public void serviceTruck(
            @PathVariable String id
    ) {

        commandProducer.sendCommand(
                id,
                "SERVICE"
        );
    }



    @PostMapping("/truck/{id}/refuel")
    public void refuelTruck(
            @PathVariable String id
    ) {

        commandProducer.sendCommand(
                id,
                "REFUEL"
        );
    }


    @PostMapping("/repair-all")
    public void repairAllTrucks() {

        fleetManager
                .getTrucks()
                .stream()
                .map(Truck::getId)
                .toList()
                .forEach(id ->
                        commandProducer.sendCommand(
                                id,
                                "ALL_REPAIR"
                        )
                );
    }



    @GetMapping("/stats")
    public FleetStats getStats() {
        return fleetManager.getStats();
    }



    @GetMapping("/truck/{id}")
    public Truck getTruck(
            @PathVariable String id
    ) {
        return fleetManager.getTruck(id);
    }

}