package com.damianos.fleet.simulator.model;

public record TruckEvent(
        String truckId,
        double latitude,
        double longitude,
        double speed,
        String status
) {
}