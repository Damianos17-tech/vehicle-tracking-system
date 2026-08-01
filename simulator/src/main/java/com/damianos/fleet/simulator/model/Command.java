package com.damianos.fleet.simulator.model;

public record Command(
        String truckId,
        String command
) {}