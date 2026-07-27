package com.damianos.fleet.vehicletracking.repository;

import com.damianos.fleet.vehicletracking.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTruckRepository extends JpaRepository<Truck, String> {

}