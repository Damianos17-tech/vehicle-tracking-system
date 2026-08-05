package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.Position;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckState.Status;
import com.damianos.fleet.vehicletracking.repository.JpaTruckRepository;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TruckInitializer implements CommandLineRunner {

    private static final int FLEET_SIZE = 1000;

    private final JpaTruckRepository jpaTruckRepository;
    private final TruckRepository truckRepository;
    private final RouteService routeService;

    public TruckInitializer(
            JpaTruckRepository jpaTruckRepository,
            TruckRepository truckRepository,
            RouteService routeService
    ) {
        this.jpaTruckRepository = jpaTruckRepository;
        this.truckRepository = truckRepository;
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {

        int attempts = 10;

        while (attempts > 0) {

            try {

                initializeTrucks();

                List<Truck> trucks = truckRepository.findAll();

                if (!trucks.isEmpty()) {

                    System.out.println("TRUCKS READY: " + trucks.size());
                    return;
                }

            } catch (Exception e) {

                System.out.println(
                        "Truck initialization failed: "
                                + e.getMessage()
                );
            }

            attempts--;

            System.out.println(
                    "Waiting for database / OSRM... attempts left: "
                            + attempts
            );

            Thread.sleep(5000);
        }

        throw new RuntimeException("Could not initialize trucks");
    }

    private void initializeTrucks() {

        long truckCount = jpaTruckRepository.count();

        if (truckCount != FLEET_SIZE) {

            System.out.println(
                    "Database contains " + truckCount +
                            " trucks. Recreating fleet..."
            );

            truckRepository.clear();
            jpaTruckRepository.deleteAll();

            List<Truck> trucks = new ArrayList<>();

            trucks.add(new Truck("TRUCK-0001", "DAF XF 460 tandem", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0002", "DAF XF 530 plandeka", 83, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0003", "MAN TGX 540 silos", 83, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0004", "Mercedes Actros 1851 cysterna", 80, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0005", "Volvo FH 500 plandeka", 92, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0006", "Renault T-High 520 plandeka", 88, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0007", "Scania R450 chłodnia", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0008", "MAN TGX 460 platforma", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-0009", "Scania R730 chłodnia", 91, Status.ACTIVE));

            for (int i = 10; i <= FLEET_SIZE; i++) {

                trucks.add(
                        new Truck(
                                String.format("TRUCK-%04d", i),
                                "Truck " + i,
                                90,
                                Status.ACTIVE
                        )
                );
            }

            for (Truck truck : trucks) {

                Position start = truck.getPosition();
                Position destination = new Position();

                truck.setRoute(
                        routeService.generateRoute(
                                start,
                                destination
                        )
                );

                truck.setOnline(false);
            }

            jpaTruckRepository.saveAll(trucks);

            for (Truck truck : trucks) {
                truckRepository.save(truck);
            }

            truckRepository.setRepoInitialized(true);

            System.out.println(
                    "Created and saved trucks: "
                            + trucks.size()
            );

        } else {

            List<Truck> trucksFromDatabase =
                    jpaTruckRepository.findAll();

            trucksFromDatabase.sort(
                    Comparator.comparing(Truck::getId)
            );

            for (Truck truck : trucksFromDatabase) {

                Position destination = new Position();

                truck.createRoute(
                        destination,
                        routeService
                );

                truck.setOnline(false);
                truck.setSimulatorId(null);

                truckRepository.save(truck);
            }

            System.out.println(
                    "Loaded trucks from database: "
                            + trucksFromDatabase.size()
            );

            truckRepository.setRepoInitialized(true);
        }
    }
}