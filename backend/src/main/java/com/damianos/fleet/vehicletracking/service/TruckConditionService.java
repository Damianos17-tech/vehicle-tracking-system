package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckState;
import com.damianos.fleet.vehicletracking.model.TruckState.*;
import com.damianos.fleet.vehicletracking.model.TruckState.Warning;
import com.damianos.fleet.vehicletracking.repository.TruckEventPublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@NoArgsConstructor
@AllArgsConstructor
@Service
public class TruckConditionService {

    private static final double FUEL_USAGE_PER_KM = 5.9;
    private static final double TECHNICAL_WEAR_PER_KM = 0.8;
    private static final double LOW_FUEL_THRESHOLD = 15.0;
    public static final double SERVICE_REQUIRED_AFTER_KM = 1_000.0;
    private static final double SERVICE_REQUIRED_CONDITION = 40.0;


    //WARNINGI
    private static final int SPEEDING_THRESHOLD = 90;
    private static final long DRIVER_BREAK_AFTER_SECONDS = 50;
    private static final long DRIVER_BREAK_DURATION_SECONDS = 10;

    private static final long LONG_IDLE_AFTER_SECONDS = 45;

    private static final double IDLE_STOP_PROBABILITY = 0.001;
    private static final long IDLE_STOP_DURATION_SECONDS = 30;

    private final TruckEventPublisher eventPublisher;

    public void updateAfterDistance(Truck truck, double distanceKm) {
        truck.setTotalDistanceKm(truck.getTotalDistanceKm() + distanceKm);
        truck.setDistanceSinceServiceKm(truck.getDistanceSinceServiceKm() + distanceKm);

        truck.setFuelLevel(Math.max(0, truck.getFuelLevel() - distanceKm * FUEL_USAGE_PER_KM));
        truck.setTechnicalCondition(Math.max(0, truck.getTechnicalCondition() - distanceKm * TECHNICAL_WEAR_PER_KM));

        double kmToService = SERVICE_REQUIRED_AFTER_KM - truck.getDistanceSinceServiceKm();
        truck.setKmToService(Math.max(0, kmToService));

        updateWarnings(truck);
    }

    private void updateFuelWarning(Truck truck) {
        if (truck.getFuelLevel() <= 0 && !truck.getFailures().contains(Failure.OUT_OF_FUEL)) {
            truck.addFailure(Failure.OUT_OF_FUEL);
            eventPublisher.publish(truck, Failure.OUT_OF_FUEL.name(), "Pusty bak - brak paliwa!");
            //truck.addEvent(Failure.OUT_OF_FUEL.name(), "Pusty bak - brak paliwa!");
            truck.removeWarning(Warning.LOW_FUEL);
            truck.setSpeed(0);
            return;
        }

        if (truck.getFuelLevel() < LOW_FUEL_THRESHOLD) {
            if(!truck.getWarnings().contains(Warning.LOW_FUEL) && !truck.getFailures().contains(Failure.OUT_OF_FUEL))
            {
                truck.addWarning(Warning.LOW_FUEL);
                eventPublisher.publish(truck, Warning.LOW_FUEL.name(), "Rezerwa! Za niedługo skończy się paliwo!");
                //truck.addEvent(Warning.LOW_FUEL.name(), "Rezerwa! Za niedługo skończy się paliwo!");
            }
        } else {
            truck.removeWarning(Warning.LOW_FUEL);
        }
    }

    private void updateServiceWarning(Truck truck) {
        if (truck.getDistanceSinceServiceKm() > SERVICE_REQUIRED_AFTER_KM
                || truck.getTechnicalCondition() < SERVICE_REQUIRED_CONDITION) {
            truck.addWarning(Warning.SERVICE_REQUIRED);
        }
    }

    public void refuel(Truck truck) {
        truck.setFuelLevel(100.0);
        eventPublisher.publish(truck, "REFUEL", "Tankowanie zakończone");
        //truck.addEvent("REFUEL", "Tankowanie zakończone");
        truck.removeWarning(Warning.LOW_FUEL);
        truck.removeFailure(Failure.OUT_OF_FUEL);
        truck.refreshStatus();
        finishDriverBreak(truck);
    }

    public void performService(Truck truck) {
        truck.setTechnicalCondition(100.0);
        truck.setDistanceSinceServiceKm(0.0);
        truck.setKmToService(SERVICE_REQUIRED_AFTER_KM);
        truck.removeWarning(Warning.SERVICE_REQUIRED);
        truck.refreshStatus();

    }

    public void repair(Truck truck) {

        performService(truck);

        // CLEAR WARNINGS / FAILURES
        truck.getFailures().clear();
        truck.refreshStatus();

    }

    public void repairAll(List<Truck> trucks) {
        trucks.forEach(this::repair);
    }

    //Tankuj
    //Serwisuj
    //Napraw
    //Napraw wszystkie



    public void generateRandomFailures(Truck truck) {
        if (!truck.getFailures().isEmpty()) { return; }

        if (shouldHaveAccident(truck)) {
            truck.addFailure(Failure.ACCIDENT);
            eventPublisher.publish(truck, Failure.ACCIDENT.name(), "Wypadek!");
            //truck.addEvent(Failure.ACCIDENT.name(), "Wypadek!");
            return;
        }

        if (shouldBreakDown(truck)) {
            truck.addFailure(Failure.BREAKDOWN);
            eventPublisher.publish(truck, Failure.BREAKDOWN.name(), "Pojazd zepsuty");
            //truck.addEvent(Failure.BREAKDOWN.name(), "Pojazd zepsuty");
        }
    }

    //Prawdopodobieństwa awarii
    private boolean shouldHaveAccident(Truck truck) {
        double probability = 0.00001; //0.0001

        if (truck.getSpeed() > 70) {
            probability += 0.000005; //0.0002
        }

        if (truck.getSpeed() > 82) {
            probability += 0.00001; //0.0005
        }

        return Math.random() < probability;
    }

    private boolean shouldBreakDown(Truck truck) {
        double probability = 0.00005;

        if (truck.getTechnicalCondition() < 70) {
            probability += 0.00001;
        }

        if (truck.getTechnicalCondition() < 50) {
            probability += 0.00003;
        }

        if (truck.getTechnicalCondition() < 40) {
            probability += 0.00006;
        }

        if (truck.getDistanceSinceServiceKm() > 10_000) {
            probability += 0.00002;
        }

        if (truck.getDistanceSinceServiceKm() > 12_000) {
            probability += 0.00005;
        }

        return Math.random() < probability;
    }

    public void updateWarnings(Truck truck) {
        updateFuelWarning(truck);
        updateServiceWarning(truck);
        updateSpeedingWarning(truck);
        updateDriverBreakWarning(truck);
        updateIdleStop(truck);
        updateLongIdleWarning(truck);
    }

    private void updateSpeedingWarning(Truck truck) {
        if (truck.getSpeed() > SPEEDING_THRESHOLD) {

            if (!truck.getWarnings().contains(Warning.SPEEDING)) {
                truck.addWarning(Warning.SPEEDING);
                eventPublisher.publish(truck, Warning.SPEEDING.name(), "Przekroczenie prędkości!");
            }

        } else {
            truck.removeWarning(Warning.SPEEDING);
        }
    }

    private void updateDriverBreakWarning(Truck truck) {
        if (truck.isPaused()) {

            truck.setSpeed(0);

            long elapsed =
                    System.currentTimeMillis()
                            - truck.getDriverBreakStartedAt();


            if(elapsed >= DRIVER_BREAK_DURATION_SECONDS * 1000) {


                truck.setDrivingTimeSinceBreakSeconds(0);

                eventPublisher.publish(truck, "BREAK_END", "Pauza zakończona. Wznawiam jazdę");
                //truck.addEvent("BREAK_END", "Pauza zakończona. Wznawiam jazdę");


                truck.removeWarning(
                        Warning.DRIVER_BREAK
                );


                truck.setPaused(false);
                truck.setDrivingStartedAt(System.currentTimeMillis());

            }


            return;
        }

        if (truck.getSpeed() > 0 && truck.getFailures().isEmpty()) {

            long drivingSeconds =
                    (System.currentTimeMillis()
                            - truck.getDrivingStartedAt())
                            / 1000;


            truck.setDrivingTimeSinceBreakSeconds(drivingSeconds);
        }

        if (truck.getDrivingTimeSinceBreakSeconds() >= DRIVER_BREAK_AFTER_SECONDS) {
            truck.addWarning(Warning.DRIVER_BREAK);
            truck.setPaused(true);

            truck.setDriverBreakStartedAt(
                    System.currentTimeMillis()
            );

            LocalDateTime until = LocalDateTime.now().plusSeconds(DRIVER_BREAK_DURATION_SECONDS);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            eventPublisher.publish(truck, Info.DRIVER_BREAK.name(), "Robię pauzę do " + until.format(formatter));
            //truck.addEvent(Info.DRIVER_BREAK.name(), "Robię pauzę do " + until.format(formatter) );
            truck.setSpeed(0);
        }
    }

    private void updateLongIdleWarning(Truck truck) {
        boolean isIdleWithoutFailure = truck.getSpeed() == 0
                && truck.getFailures().isEmpty();

        if (isIdleWithoutFailure) {
            truck.setIdleTimeSeconds(truck.getIdleTimeSeconds() + 1);
        } else {
            truck.setIdleTimeSeconds(0);
            truck.removeWarning(Warning.LONG_IDLE);
        }

        if (truck.getIdleTimeSeconds() >= LONG_IDLE_AFTER_SECONDS) {
            truck.addWarning(Warning.LONG_IDLE);
        }
    }

    public void finishDriverBreak(Truck truck) {
        truck.setDrivingTimeSinceBreakSeconds(0);
        truck.removeWarning(Warning.DRIVER_BREAK);
        truck.setPaused(false);
        truck.setDrivingStartedAt(
                System.currentTimeMillis()
        );
    }

    //Kierowca leci w chuja
    private void updateIdleStop(Truck truck) {
        /*
        if (truck.getFailures().isEmpty()
                && !truck.isPaused()
                && truck.getIdleStopSeconds() == 0
                && truck.getSpeed() > 0
                && Math.random() < IDLE_STOP_PROBABILITY) {
            truck.setIdleStopSeconds(1);
            truck.setSpeed(0);
            return;
        }

        if (truck.getIdleStopSeconds() > 0) {
            truck.setIdleStopSeconds(truck.getIdleStopSeconds() + 1);
            truck.setSpeed(0);

            if (truck.getIdleStopSeconds() >= IDLE_STOP_DURATION_SECONDS) {
                truck.setIdleStopSeconds(0);
            }
        }

         */
    }


}