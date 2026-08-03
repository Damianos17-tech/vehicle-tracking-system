package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.kafka.TruckCommandProducer;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.repository.TruckEventPublisher;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class FleetAutoRepairService {


    private final TruckRepository truckRepository;
    private final TruckConditionService truckConditionService;
    private final TruckEventPublisher eventPublisher;
    private final TruckCommandProducer commandProducer;


    @Scheduled(fixedDelay = 10000) // co 10 sekund
    public void checkFleet() {

        //List<Truck> trucks = truckRepository.findAll();
        List<Truck> trucks = new ArrayList<>(truckRepository.findAll());


        boolean allStopped =
                trucks.stream()
                        .allMatch(truck ->
                                truck.getSpeed() == 0 && !truck.isPaused()
                        );


        if(allStopped && !trucks.isEmpty()) {

            System.out.println(
                    "ALL TRUCKS STOPPED - AUTO REPAIR"
            );


            //truckConditionService.repairAll(trucks);

            trucks.forEach(truck -> {
//                System.out.println(
//                        truck.getId() +
//                                " speed=" + truck.getSpeed() +
//                                " fuel=" + truck.getFuelLevel() +
//                                " paused=" + truck.isPaused()
//                );

                truckConditionService.repair(truck);
                truckConditionService.refuel(truck);
                // OPTIONAL: event log

                eventPublisher.publish(truck, "SYSTEM_RECOVERY", "Automatic Fleet emergency recovery - vehicle repaired");

                truckRepository.save(truck);

                //commandProducer.sendCommand(truck.getId(), "REPAIR");
                //commandProducer.sendCommand(truck.getId(), "REFUEL");
                commandProducer.sendCommand(truck.getId(), "SYSTEM_RECOVERY");




            });

        }

    }

}