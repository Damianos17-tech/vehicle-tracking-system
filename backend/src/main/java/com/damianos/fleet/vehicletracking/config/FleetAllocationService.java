package com.damianos.fleet.vehicletracking.config;


import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.repository.JpaTruckRepository;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class FleetAllocationService {


    private static final int TRUCKS_PER_SIMULATOR = 100;

    private static final long HEARTBEAT_TIMEOUT = 15000;


    private final TruckRepository truckRepository;

    private final JpaTruckRepository jpaTruckRepository;



    /*
        Lista aktywnych simulatorów
     */
    private final List<String> simulators =
            new ArrayList<>();



    /*
        simulatorId -> ostatni heartbeat
     */
    private final Map<String, Long> heartbeats =
            new ConcurrentHashMap<>();





    public FleetAllocationService(
            TruckRepository truckRepository,
            JpaTruckRepository jpaTruckRepository
    ) {

        this.truckRepository = truckRepository;
        this.jpaTruckRepository = jpaTruckRepository;

    }






    public synchronized List<Truck> assignTrucks(String simulatorId) {



        if (!truckRepository.isRepoInitialized()) {

            System.out.println(
                    "Truck repository not initialized yet"
            );

            return new ArrayList<>();

        }




        System.out.println(
                "REGISTER SIMULATOR: "
                        + simulatorId
        );





        /*
            Simulator już istnieje
        */
        if(simulators.contains(simulatorId)){


            heartbeats.put(
                    simulatorId,
                    System.currentTimeMillis()
            );


            System.out.println(
                    "Simulator already registered: "
                            + simulatorId
            );


            return List.of();

        }







        simulators.add(simulatorId);



        heartbeats.put(
                simulatorId,
                System.currentTimeMillis()
        );





        List<Truck> allTrucks =
                new ArrayList<>(truckRepository.findAll());



        allTrucks.sort(
                Comparator.comparing(Truck::getId)
        );



        List<Truck> assigned =
                new ArrayList<>();






        for(Truck truck : allTrucks){



            if(!truck.isOnline()){


                assigned.add(truck);



                if(assigned.size() >= TRUCKS_PER_SIMULATOR){

                    break;

                }

            }

        }







        if(assigned.isEmpty()){


            System.out.println(
                    "NO AVAILABLE TRUCKS"
            );



            simulators.remove(simulatorId);

            heartbeats.remove(simulatorId);



            return List.of();

        }







        for(Truck truck : assigned){


            truck.setOnline(true);

            truck.setSimulatorId(simulatorId);



            truckRepository.save(truck);

            jpaTruckRepository.save(truck);



            System.out.println(
                    "ASSIGNED "
                            + truck.getId()
            );


        }







        System.out.println(
                "Simulator "
                        + simulatorId
                        + " received "
                        + assigned.size()
                        + " trucks"
        );



        return assigned;

    }








    /*
        HEARTBEAT + LEASE VALIDATION
     */
    public boolean heartbeat(String simulatorId) {


        if(simulators.contains(simulatorId)) {


            heartbeats.put(
                    simulatorId,
                    System.currentTimeMillis()
            );


            System.out.println(
                    "HEARTBEAT OK: "
                            + simulatorId
            );


            return true;

        }



        System.out.println(
                "UNKNOWN SIMULATOR: "
                        + simulatorId
        );


        return false;

    }









    /*
        Sprawdzanie martwych simulatorów
     */
    @Scheduled(fixedDelay = 5000)
    public synchronized void checkHeartbeats(){



        long now =
                System.currentTimeMillis();




        List<String> dead =
                new ArrayList<>();




        heartbeats.forEach(
                (simulatorId,last) -> {


                    if(now - last > HEARTBEAT_TIMEOUT){


                        dead.add(simulatorId);

                    }


                }
        );







        for(String simulatorId : dead){



            System.out.println(
                    "SIMULATOR DEAD "
                            + simulatorId
            );






            List<Truck> trucks =
                    truckRepository.findAll();






            for(Truck truck : trucks){



                if(simulatorId.equals(
                        truck.getSimulatorId()
                )){


                    truck.setOnline(false);

                    truck.setSimulatorId(null);



                    truckRepository.save(truck);

                    jpaTruckRepository.save(truck);



                    System.out.println(
                            "RELEASED "
                                    + truck.getId()
                    );


                }


            }






            simulators.remove(simulatorId);

            heartbeats.remove(simulatorId);





            System.out.println(
                    "REMOVED SIMULATOR "
                            + simulatorId
            );


        }



    }








    public synchronized long getOnlineTruckCount(){



        return truckRepository.findAll()

                .stream()

                .filter(Truck::isOnline)

                .count();


    }








    public synchronized List<Truck> getOnlineTrucks(){


        List<Truck> result =
                new ArrayList<>();



        for(Truck truck : truckRepository.findAll()){


            if(truck.isOnline()){

                result.add(truck);

            }


        }



        return result;

    }



}