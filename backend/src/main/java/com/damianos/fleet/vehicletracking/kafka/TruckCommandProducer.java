package com.damianos.fleet.vehicletracking.kafka;

import com.damianos.fleet.vehicletracking.model.Command;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class TruckCommandProducer {


    private final KafkaTemplate<String, Command> kafkaTemplate;


    public TruckCommandProducer(
            KafkaTemplate<String, Command> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }



    public void sendCommand(
            String truckId,
            String command
    ) {


        Command cmd = new Command(
                truckId,
                command
        );


        kafkaTemplate.send(
                "truck-commands",
                truckId,
                cmd
        );


        //System.out.println("KAFKA COMMAND SENT: " + command + " -> " + truckId);
    }
}