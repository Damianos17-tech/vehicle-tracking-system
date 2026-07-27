package com.damianos.fleet.vehicletracking.repository;

import com.damianos.fleet.vehicletracking.model.Truck;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.damianos.fleet.vehicletracking.model.TruckState.Status;
import com.damianos.fleet.vehicletracking.model.TruckState.Warning;
import com.damianos.fleet.vehicletracking.model.TruckState.Failure;

@Repository
public class TruckRepository {


    private final List<Truck> trucks = new ArrayList<>();

    @PostConstruct
    public void init() {


    }

    public List<Truck> findAll() {
        return trucks;
    }


    public Truck findById(String id) {
        return trucks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Truck save(Truck truck) {
        trucks.removeIf(t -> Objects.equals(t.getId(), truck.getId()));
        trucks.add(truck);
        return truck;
    }

}
