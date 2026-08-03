package com.damianos.fleet.simulator.model;


import com.damianos.fleet.simulator.service.RouteService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.damianos.fleet.simulator.model.TruckState.Status;
import com.damianos.fleet.simulator.model.TruckState.Warning;
import com.damianos.fleet.simulator.model.TruckState.Failure;
import org.springframework.scheduling.config.Task;






import com.damianos.fleet.simulator.model.TruckState.Status;

@Data
public class Truck {


    private String id;
    private String name;
    private int maxSpeed;
    private TruckState.Status status = TruckState.Status.ACTIVE;

    private int speed;
    @Getter
    @Setter
    private Position position;
    @JsonIgnore
    private List<Position> route;
    private int routeIndex = 0;
    private long lastUpdateTime = System.currentTimeMillis();

    //Dokładamy bajery

    private double fuelLevel = 30 + Math.random() * 70;; // procent
    private double totalDistanceKm = 0.0;
    private double distanceSinceServiceKm = 0.0;
    private double technicalCondition = 100.0; // 0-100
    private double lastDistanceKm = 0.0;


    private List<TruckState.Warning> warnings = new ArrayList<>();
    private List<TruckState.Failure> failures = new ArrayList<>();
    private List<TruckEvent> events = new ArrayList<>();

    private long drivingTimeSinceBreakSeconds = 0;
    private long idleTimeSeconds = 0;
    private long lastStatusChangeTime = System.currentTimeMillis();

    private long driverBreakSeconds = 0;
    private long idleStopSeconds = 0;
    private double kmToService = 0;

    //private Task currentTask;
    private static final int MAX_EVENTS = 15;
    @Setter
    @Getter
    private boolean paused = false;
    private long driverBreakStartedAt;
    private long drivingStartedAt = System.currentTimeMillis();

    private long lastSpeedingEvent = 0;


    public Truck() {
    }

    public Truck(String id, String name, int maxSpeed, Status status) {

        this.id = id;
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.status = status;
        if (!canMove()) { this.maxSpeed = 0; this.speed = 0; }

        // random Warszawa + okolice
        this.position = new Position();
        this.drivingStartedAt = System.currentTimeMillis();
    }

    public void createRoute(Position destination, RouteService routeService) {
        this.route = routeService.generateRoute(this.position, destination);
        this.routeIndex = 0;
    }

    public void refreshStatus() {
        if (this.status == Status.INACTIVE) {
            return;
        }

        if (!failures.isEmpty()) {
            this.status = Status.FAILURE;
        } else if (!warnings.isEmpty()) {
            this.status = Status.WARNING;
        } else {
            this.status = Status.ACTIVE;
        }
    }

    public Position nextPosition() {

        if (route == null || route.isEmpty()) {
            this.speed = 0;
            this.lastDistanceKm = 0.0;
            return null;
        }
        if (!canMove()) { this.speed = 0; this.lastDistanceKm = 0.0; return this.position; }
        long now = System.currentTimeMillis();

        //Position next = route.get(routeIndex);
        if(routeIndex >= route.size()){
            reverseRoute();
        }

        Position next = route.get(routeIndex);


        // oblicz dystans (w metrach)
        double distance = calculateDistance(this.position, next);
        this.lastDistanceKm = distance / 1000.0;

        // czas w sekundach
        double timeSec = (now - lastUpdateTime) / 1000.0;

        if (timeSec > 0) {
            //this.speed = (int) ((distance / timeSec) * 3.6); // m/s -> km/h
            int newSpeed = (int) ((distance / timeSec) * 3.6);
            int calculatedSpeed = (this.speed + newSpeed) / 2;
            this.speed = Math.min(this.maxSpeed, calculatedSpeed);
        }

        lastUpdateTime = now;

        //routeIndex++;
        routeIndex += 5;

        if (routeIndex >= route.size()) {
            reverseRoute();
        }

        this.position = next;
        return next;
    }

    private void generateNewRoute(RouteService routeService) {
        Position start = this.position;
        Position destination = new Position(); // albo z bazy punktów
        this.route = routeService.generateRoute(start, destination);
        this.routeIndex = 0;
    }


    public String posToString() {
        return "(" + position.getLatitude() + ", " + position.getLongitude() + ")";
    }

    private void reverseRoute() {
        if (route == null || route.isEmpty()) return;

        java.util.Collections.reverse(route);
        routeIndex = 0;
    }

    private double calculateDistance(Position p1, Position p2) {

        double R = 6371000; // promień Ziemi w metrach

        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());

        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c; // metry
    }


    public boolean canMove() {
        return status != Status.INACTIVE
                && failures.isEmpty()
                && !paused
                && !warnings.contains(Warning.GPS_LOST)
                && idleStopSeconds == 0;
    }



    public void addWarning(TruckState.Warning warning) {
        if (!warnings.contains(warning)) {
            warnings.add(warning);
            //addEvent(warning.name(), "Warning added: " + warning);

            if (warnings.size() > MAX_EVENTS) {
                warnings.remove(0);
            }
        }
        refreshStatus();
    }

    public void removeWarning(TruckState.Warning warning) {
        warnings.remove(warning);
        refreshStatus();
    }

    public void addFailure(TruckState.Failure failure) {
        if (!failures.contains(failure)) {
            failures.add(failure);
            //addEvent(failure.name(), "Failure added: " + failure);

            if (failures.size() > MAX_EVENTS) {
                failures.remove(0);
            }
        }
        this.speed = 0;
        refreshStatus();
    }

    public void removeFailure(TruckState.Failure failure) {
        failures.remove(failure);
        refreshStatus();
    }


//
//    public void addEvent(String type, String message) {
//        events.add(new TruckEvent(this.id, type, message, LocalDateTime.now()));
//        if (events.size() > MAX_EVENTS) {
//            events.remove(0);
//        }
//    }

    public void addEvent(TruckEvent event) {

        events.add(event);

        if (events.size() > MAX_EVENTS) {
            events.remove(0);
        }
    }

    public String getEventsText() {
        if (events.isEmpty()) {
            return "[]";
        }

        return events.stream()
                .map(TruckEvent::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }


    public void setFuelLevel(double fuelLevel){
        this.fuelLevel = Math.round(fuelLevel * 10.0) / 10.0;
    }


    @Override
    public String toString() {
        return "Truck{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxSpeed=" + maxSpeed +
                ", status=" + status +
                ", speed=" + speed +
                ", position=" + position +

                ", routeIndex=" + routeIndex +
                ", lastUpdateTime=" + lastUpdateTime +
                ", fuelLevel=" + fuelLevel +
                ", totalDistanceKm=" + totalDistanceKm +
                ", distanceSinceServiceKm=" + distanceSinceServiceKm +
                ", technicalCondition=" + technicalCondition +
                ", lastDistanceKm=" + lastDistanceKm +
                ", warnings=" + warnings +
                ", failures=" + failures +
                ", events=" + events +
                ", drivingTimeSinceBreakSeconds=" + drivingTimeSinceBreakSeconds +
                ", idleTimeSeconds=" + idleTimeSeconds +
                ", lastStatusChangeTime=" + lastStatusChangeTime +
                ", driverBreakSeconds=" + driverBreakSeconds +
                ", idleStopSeconds=" + idleStopSeconds +
                ", kmToService=" + kmToService +
                '}';
    }


}