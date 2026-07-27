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
public class TruckRepositoryOld {


    private final List<Truck> trucks = new ArrayList<>();

    @PostConstruct
    public void init() {
        trucks.add(new Truck("TRUCK-001", "DAF XF 460 tandem", 90, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-002", "DAF XF 530 plandeka", 83, Status.ACTIVE));
        //trucks.add(new Truck("TRUCK-003", "Truck Gamma", 0, Truck.Status.IDLE));


        trucks.add(new Truck("TRUCK-004", "Mercedes Actros 1851 cysterna", 80, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-005", "Volvo FH 500 plandeka", 92, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-006", "Renault T-High 520 plandeka", 88, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-007", "Scania R450 chłodnia", 90, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-008", "MAN TGX 460 platforma", 90, Status.ACTIVE));
        trucks.add(new Truck("TRUCK-009", "Scania R730 chłodnia", 91, Status.ACTIVE));

        for(int i=1; i<150; i++){
            trucks.add(new Truck("TRUCK-"+i, "Truck "+i, 90, Status.ACTIVE));
        }

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
