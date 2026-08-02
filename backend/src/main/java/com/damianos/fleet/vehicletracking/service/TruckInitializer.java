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


                if (!truckRepository.findAll().isEmpty()) {

                    System.out.println("TRUCKS READY: "
                            + truckRepository.findAll().size());

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


        throw new RuntimeException(
                "Could not initialize trucks"
        );
    }



    private void initializeTrucks() {


        int fleetSize = 300;

        // BAZA JEST PUSTA -> TWORZYMY PIERWSZĄ FLOTĘ

        if (jpaTruckRepository.count() == 0 || jpaTruckRepository.count() < fleetSize) {

            truckRepository.clear();
            jpaTruckRepository.deleteAll();

            List<Truck> trucks = new ArrayList<>();


            trucks.add(new Truck("TRUCK-001", "DAF XF 460 tandem", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-002", "DAF XF 530 plandeka", 83, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-003", "MAN TGX 540 silos", 83, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-004", "Mercedes Actros 1851 cysterna", 80, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-005", "Volvo FH 500 plandeka", 92, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-006", "Renault T-High 520 plandeka", 88, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-007", "Scania R450 chłodnia", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-008", "MAN TGX 460 platforma", 90, Status.ACTIVE));
            trucks.add(new Truck("TRUCK-009", "Scania R730 chłodnia", 91, Status.ACTIVE));




            for (int i = 10; i <= fleetSize; i++) {

                trucks.add(
                        new Truck(
                                String.format("TRUCK-%03d", i),
                                "Truck " + i,
                                90,
                                Status.ACTIVE
                        )
                );
            }


            trucks.forEach(truck -> {

                Position start = truck.getPosition();

                Position destination = new Position();

                truck.setRoute(
                    routeService.generateRoute(
                        start,
                        destination
                    )
                );

                truck.setOnline(false);

            });


            jpaTruckRepository.saveAll(trucks);


            trucks.forEach(truckRepository::save);


            System.out.println(
                    "Created and saved trucks: "
                    + trucks.size()
            );


        } else {


            // BAZA MA JUŻ DANE -> WCZYTUJEMY


            List<Truck> trucksFromDatabase =
                    jpaTruckRepository.findAll();

            trucksFromDatabase.sort(
                    Comparator.comparing(Truck::getId)
            );


            trucksFromDatabase.forEach(truck -> {


                Position start = truck.getPosition();

                Position destination = new Position();


                truck.createRoute(
                        destination,
                        routeService
                );


                truck.setOnline(false);
                truck.setSimulatorId(null);

                truckRepository.save(truck);

            });


            System.out.println(
                    "Loaded trucks from database: "
                    + trucksFromDatabase.size()
            );

            truckRepository.setRepoInitialized(true);
        }
    }
}