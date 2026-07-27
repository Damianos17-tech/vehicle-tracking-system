package com.damianos.fleet.vehicletracking.model;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.persistence.Embeddable;
@Embeddable
public class Position {

    private double latitude;
    private double longitude;

    // Granice obszaru Warszawy + niewielkie okolice
    private static final double MIN_LAT = 52.10;
    private static final double MAX_LAT = 52.35;

    private static final double MIN_LNG = 20.85;
    private static final double MAX_LNG = 21.30;

    public Position() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        this.latitude = random.nextDouble(MIN_LAT, MAX_LAT);
        this.longitude = random.nextDouble(MIN_LNG, MAX_LNG);
    }

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}