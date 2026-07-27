package com.damianos.fleet.vehicletracking.controller;

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

    public TruckController(FleetManager fleetManager) {
        this.fleetManager = fleetManager;
    }

    @GetMapping
    public List<Truck> getTrucks() {
        return fleetManager.getTrucks();
    }



    @PostMapping("/repair-all")
    public void repairAllTrucks() {
        fleetManager.repairAllTrucks();
    }

    @PostMapping("/truck/{id}/repair")
    public void repairTruck(@PathVariable String id) {
        fleetManager.repairTruck(id);
    }

    @PostMapping("/truck/{id}/service")
    public void serviceTruck(@PathVariable String id) {
        fleetManager.serviceTruck(id);
    }

    @PostMapping("/truck/{id}/refuel")
    public Truck refuelTruck(@PathVariable String id) {
        return fleetManager.refuelTruck(id);
    }

    @GetMapping("/stats")
    public FleetStats getStats() {
        return fleetManager.getStats();
    }

    @GetMapping("/truck/{id}")
    public Truck getTruck(@PathVariable String id) {
        return fleetManager.getTruck(id);
    }

}