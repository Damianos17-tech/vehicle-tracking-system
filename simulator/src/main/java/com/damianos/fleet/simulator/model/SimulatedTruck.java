package com.damianos.fleet.simulator.model;

import lombok.Data;

@Data
public class SimulatedTruck {

    private String id;

    private String name;

    private int maxSpeed;

    private int speed;

    private Position position;

    private double fuelLevel;

    public SimulatedTruck(
            String id,
            String name,
            Position position
    ) {
        this.id = id;
        this.name = name;
        this.maxSpeed = 90;
        this.speed = 0;
        this.position = position;
        this.fuelLevel = 100;
    }

    public void move() {
        // zmiana pozycji
        // aktualizacja prędkości
    }


    public void consumeFuel() {
        fuelLevel = Math.max(0, fuelLevel - 0.01);
    }


}