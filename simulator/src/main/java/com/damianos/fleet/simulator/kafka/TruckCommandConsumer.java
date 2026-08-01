package com.damianos.fleet.simulator.kafka;

import com.damianos.fleet.simulator.model.Command;
import com.damianos.fleet.simulator.model.Truck;
import com.damianos.fleet.simulator.service.FleetManager;
import com.damianos.fleet.simulator.service.TruckConditionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class TruckCommandConsumer {


    private final FleetManager fleetManager;
    private final TruckConditionService truckConditionService;
    private final TruckEventProducer eventProducer;


    public TruckCommandConsumer(
            FleetManager fleetManager,
            TruckConditionService truckConditionService, TruckEventProducer eventProducer
    ) {
        this.fleetManager = fleetManager;
        this.truckConditionService = truckConditionService;
        this.eventProducer = eventProducer;
    }



    @KafkaListener(
            topics = "truck-commands",
            groupId = "simulator"
    )
    public void consume(Command command) {


        System.out.println(
                "COMMAND RECEIVED: "
                        + command.truckId()
                        + " -> "
                        + command.command()
        );


        Truck truck =
                fleetManager.getTruckById(
                        command.truckId()
                );

//        if ("ALL_REPAIR".equals(command.command())) {
//
//            fleetManager.getTrucks().forEach(gt -> {
//                truckConditionService.repair(gt);
//                truckConditionService.refuel(gt);
//            });
//
//            return;
//        }

        if (truck == null) {

            System.out.println(
                    "Truck not found: "
                            + command.truckId()
            );

            return;
        }



        switch(command.command()) {


            case "REFUEL":

                truckConditionService.refuel(truck);

                break;


            case "SERVICE":

                truckConditionService.performService(truck);

                break;


            case "REPAIR":

                truckConditionService.repair(truck);

                break;


            case "ALL_REPAIR":

                truckConditionService.repair(truck);
                truckConditionService.refuel(truck);

                break;

            case "SYSTEM_RECOVERY":

                truckConditionService.repair(truck);
                truckConditionService.refuel(truck);

                eventProducer.sendEvent(
                        truck,
                        "SYSTEM_RECOVERY",
                        "Automatic Fleet emergency recovery"
                );

                break;


            default:

                System.out.println(
                        "Unknown command: "
                                + command.command()
                );
        }
    }
}