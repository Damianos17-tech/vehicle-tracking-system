package com.damianos.fleet.simulator.model;

import java.util.concurrent.ThreadLocalRandom;



public class Position {

    private double latitude;
    private double longitude;


    private static final double MIN_LAT = 52.10;
    private static final double MAX_LAT = 52.35;

    private static final double MIN_LNG = 20.85;
    private static final double MAX_LNG = 21.30;


    public Position() {
        randomize();
    }


    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    private void randomize() {

        ThreadLocalRandom random =
                ThreadLocalRandom.current();


        this.latitude =
                random.nextDouble(MIN_LAT, MAX_LAT);

        this.longitude =
                random.nextDouble(MIN_LNG, MAX_LNG);

    }



    public static Position generateDestination(Position start) {


        ThreadLocalRandom random =
                ThreadLocalRandom.current();


        Position destination;


        do {


            double lat =
                    random.nextDouble(MIN_LAT, MAX_LAT);


            double lng =
                    random.nextDouble(MIN_LNG, MAX_LNG);


            destination =
                    new Position(lat, lng);



        } while(distanceKm(start, destination) < 20);



        return destination;

    }





    private static double distanceKm(
            Position a,
            Position b
    ) {


        double earthRadius = 6371;


        double dLat =
                Math.toRadians(
                        b.latitude - a.latitude
                );


        double dLng =
                Math.toRadians(
                        b.longitude - a.longitude
                );


        double x =
                Math.sin(dLat / 2)
                        *
                        Math.sin(dLat / 2)
                        +
                        Math.cos(
                                Math.toRadians(a.latitude)
                        )
                                *
                                Math.cos(
                                        Math.toRadians(b.latitude)
                                )
                                *
                                Math.sin(dLng / 2)
                                *
                                Math.sin(dLng / 2);


        double c =
                2 *
                        Math.atan2(
                                Math.sqrt(x),
                                Math.sqrt(1-x)
                        );


        return earthRadius * c;

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