package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.*;
import com.damianos.fleet.vehicletracking.repository.TruckEventPublisher;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.damianos.fleet.vehicletracking.service.TruckConditionService.SERVICE_REQUIRED_AFTER_KM;

@Service
public class FleetManager {

    private final TruckRepository repo;
    private final RouteService routeService;
    private final TruckConditionService truckConditionService;
    private final TruckEventPublisher eventPublisher;

    public FleetManager(TruckRepository repo, RouteService routeService, TruckConditionService truckConditionService,
                        TruckEventPublisher eventPublisher) {
        this.repo = repo;
        this.routeService = routeService;
        this.truckConditionService = truckConditionService;
        this.eventPublisher = eventPublisher;
    }

    public List<Truck> getTrucks() {
        List<Truck> trucks = repo.findAll();

        //System.out.println("TRUCK COUNT = " + trucks.size());

        //trucks.forEach(t -> System.out.println("TRUCK FROM REPO: " + t.getId()));

        return trucks;
    }


//    @PostConstruct
//    public void initRoutes() {
//        repo.findAll().forEach(truck -> {
//
//            // losowy start (Warszawa okolice)
//            Position start = new Position();
//
//            // losowy cel
//            Position end = new Position();
//
//            // OSRM route
//            List<Position> route = routeService.generateRoute(start, end);
//
//            truck.setPosition(start);
//            truck.setRoute(route);
//        });
//    }

    public void repairAllTrucks() {

        List<Truck> trucks = this.repo.findAll();

        trucks.forEach(truck -> {
            truckConditionService.repair(truck);
            truckConditionService.refuel(truck);
            // OPTIONAL: event log

            eventPublisher.publish(truck, "ALL_RESET", "Truck fully repaired and warnings cleared");
            //truck.addEvent("ALL_RESET", "Truck fully repaired and warnings cleared");

        });

        // jeśli repo NIE jest JPA → potrzebujesz tego:
        // repo.saveAll(trucks);
    }

    public Truck repairTruck(String id) {
        Truck truck = repo.findById(id);
        truckConditionService.repair(truck);
        eventPublisher.publish(truck, "REPAIR", "Pojazd naprawiony pomyślnie");
        //truck.addEvent("REPAIR", "Pojazd naprawiony pomyślnie");
        return repo.save(truck);
    }

    public Truck serviceTruck(String id) {
        Truck truck = repo.findById(id);
        truckConditionService.performService(truck);
        eventPublisher.publish(truck, "Service performed", "Serwis zakończony, warningi usunięte");
        //truck.addEvent("Service performed", "Serwis zakończony, warningi usunięte");
        return repo.save(truck);
    }

    public Truck refuelTruck(String id) {
        Truck truck = repo.findById(id);
        truckConditionService.refuel(truck);
        //truck.setFuelLevel(100);
        //truck.addEvent("REFUEL", "Tankowanie zakończone");
        return repo.save(truck);
    }














    public FleetStats getStats() {

        //List<Truck> trucks = repo.findAll();
        List<Truck> trucks = List.copyOf(repo.findAll());

        long broken = trucks.stream()
                .filter(t -> t.getStatus() == TruckState.Status.FAILURE)
                .count();

        long warnings = trucks.stream()
                .filter(t -> t.getWarnings() != null && !t.getWarnings().isEmpty())
                .count();

        long stopped = trucks.stream()
                .filter(t -> t.getSpeed() == 0)
                .count();

        long paused = trucks.stream()
                .filter(t -> t.isPaused())
                .count();

        FleetStats stats = new FleetStats();
        stats.setTotal(trucks.size());
        stats.setBroken(broken);
        stats.setWarnings(warnings);
        stats.setStopped(stopped);
        stats.setPaused(paused);

        return stats;
    }

    public Truck getTruck(String id) {
        return repo.findById(id);
    }
}