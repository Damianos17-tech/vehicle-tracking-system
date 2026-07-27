package com.damianos.fleet.vehicletracking.model;

public class TruckState {

    public enum Status {
        ACTIVE,
        WARNING,
        FAILURE,
        INACTIVE
    }

    public enum Warning {
        LOW_FUEL,
        SERVICE_REQUIRED,
        GPS_LOST,
        DRIVER_BREAK,
        SPEEDING,
        LONG_IDLE,
        ROUTE_DELAY,

    }

    public enum Failure {
        BREAKDOWN,
        ACCIDENT,
        OUT_OF_FUEL
    }

    public enum Info {
        DRIVER_BREAK
    }
}