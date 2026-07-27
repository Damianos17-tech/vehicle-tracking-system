package com.damianos.fleet.vehicletracking.repository;

import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTruckEventRepository extends JpaRepository<TruckEvent, String> {

}