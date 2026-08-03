package com.damianos.fleet.vehicletracking.controller;

import com.damianos.fleet.vehicletracking.config.FleetAllocationService;
import com.damianos.fleet.vehicletracking.kafka.TruckCommandProducer;
import com.damianos.fleet.vehicletracking.model.FleetStats;
import com.damianos.fleet.vehicletracking.model.InfrastructureStats;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import com.damianos.fleet.vehicletracking.service.FleetManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/fleet")
public class TruckController {

    private final FleetAllocationService fleetAllocationService;

   // private final FleetManager fleetManager;
    private final TruckCommandProducer commandProducer;

    private final TruckRepository truckRepository;


    public TruckController(
            FleetAllocationService fleetAllocationService, FleetManager fleetManager,
            TruckCommandProducer commandProducer, TruckRepository truckRepository
    ) {
        this.fleetAllocationService = fleetAllocationService;
        //this.fleetManager = fleetManager;
        this.commandProducer = commandProducer;
        this.truckRepository = truckRepository;
    }



    @GetMapping
    public List<Truck> getTrucks() {
        return truckRepository.findAll();
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

        System.out.println("REFUEL REQUEST FROM FRONTEND: " + id);

        commandProducer.sendCommand(
                id,
                "REFUEL"
        );
    }


    @PostMapping("/repair-all")
    public void repairAllTrucks() {

        truckRepository
                .findAll()
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

        FleetStats stats = truckRepository.getStats();

        stats.setOnline(fleetAllocationService.getOnlineTruckCount());

        return stats;
    }


    @GetMapping("/truck/{id}")
    public Truck getTruck(
            @PathVariable String id
    ) {
        return truckRepository.findById(id);
    }


    @GetMapping("/register")
    public List<Truck> registerSimulator(
            @RequestParam String simulatorId) {

        return fleetAllocationService.assignTrucks(simulatorId);
    }


    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(
            @RequestParam String simulatorId
    ){

        boolean active =
                fleetAllocationService.heartbeat(simulatorId);


        if(!active){

            return ResponseEntity
                    .status(410)
                    .build();

        }


        return ResponseEntity.ok().build();
    }


    @GetMapping("/online")
    public List<Truck> getOnlineTrucks() {

        return fleetAllocationService.getOnlineTrucks();

    }

}