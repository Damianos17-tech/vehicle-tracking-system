package com.damianos.fleet.vehicletracking.repository;

import com.damianos.fleet.vehicletracking.model.FleetStats;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckState;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.damianos.fleet.vehicletracking.model.TruckState.Status;
import com.damianos.fleet.vehicletracking.model.TruckState.Warning;
import com.damianos.fleet.vehicletracking.model.TruckState.Failure;

@Repository
public class TruckRepository {


    private final List<Truck> trucks = new ArrayList<>();
    @Getter
    @Setter
    private boolean repoInitialized = false;


    @PostConstruct
    public void init() {


    }

    public List<Truck> findAll() {

        List<Truck> sortedTrucks = new ArrayList<>(trucks);

        sortedTrucks.sort(
                Comparator.comparing(Truck::getId)
        );

        return sortedTrucks;
    }


    public Truck findById(String id) {
        return trucks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized Truck save(Truck truck) {

        trucks.removeIf(t -> Objects.equals(t.getId(), truck.getId()));
        trucks.add(truck);

        return truck;
    }


    public void clear() {
        trucks.clear();
    }

    public FleetStats getStats() {

        List<Truck> repoCopy = List.copyOf(trucks);

        long broken = repoCopy.stream()
                .filter(t -> t.getStatus() == TruckState.Status.FAILURE)
                .count();

        long warnings = repoCopy.stream()
                .filter(t -> t.getWarnings() != null && !t.getWarnings().isEmpty())
                .count();

        long stopped = repoCopy.stream()
                .filter(t -> t.getSpeed() == 0)
                .count();

        long paused = repoCopy.stream()
                .filter(t -> t.isPaused())
                .count();

        long online = repoCopy.stream()
                .filter(Truck::isOnline)
                .count();

        FleetStats stats = new FleetStats();

        stats.setTotal(repoCopy.size());
        stats.setBroken(broken);
        stats.setWarnings(warnings);
        stats.setStopped(stopped);
        stats.setPaused(paused);
        stats.setOnline(online);

        return stats;
    }

    public List<Truck> findAvailableTrucks(int limit) {

        return new ArrayList<>(
                trucks.stream()
                        .filter(truck -> !truck.isOnline())
                        .sorted(Comparator.comparing(Truck::getId))
                        .limit(limit)
                        .toList()
        );

    }






}
