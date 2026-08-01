package com.damianos.fleet.vehicletracking.model;

public record Command(
        String truckId,
        String command
) {}