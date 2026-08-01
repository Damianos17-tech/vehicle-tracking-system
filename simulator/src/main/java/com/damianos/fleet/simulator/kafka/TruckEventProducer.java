package com.damianos.fleet.simulator.kafka;

import com.damianos.fleet.simulator.model.Truck;
import com.damianos.fleet.simulator.model.TruckEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class TruckEventProducer {


    private final KafkaTemplate<String, Truck> truckKafkaTemplate;

    private final KafkaTemplate<String, TruckEvent> eventKafkaTemplate;


    private static final String TRUCK_TOPIC = "truck-state";

    private static final String EVENT_TOPIC = "truck-events";


    public TruckEventProducer(
            KafkaTemplate<String, Truck> truckKafkaTemplate, KafkaTemplate<String, TruckEvent> eventKafkaTemplate

    ) {
        this.truckKafkaTemplate = truckKafkaTemplate;
        this.eventKafkaTemplate = eventKafkaTemplate;
    }


    public void send(Truck truck) {

        //System.out.println("KAFKA SEND TRUCK: " + truck.getId());

        truckKafkaTemplate.send(
                TRUCK_TOPIC,
                truck.getId(),
                truck
        ).whenComplete((result, exception) -> {

            if (exception != null) {
                System.out.println("❌ KAFKA ERROR");
                exception.printStackTrace();
                return;
            }

//            System.out.println(
//                    "✅ KAFKA SUCCESS topic=" +
//                            result.getRecordMetadata().topic() +
//                            " partition=" +
//                            result.getRecordMetadata().partition() +
//                            " offset=" +
//                            result.getRecordMetadata().offset()
//            );
        });
    }


    public void sendEvent(Truck truck, String type, String message) {

        TruckEvent event = new TruckEvent(
                truck.getId(),
                type,
                message,
                Instant.now()
        );

        //System.out.println("KAFKA SEND EVENT: " + event.toString());

        eventKafkaTemplate.send(EVENT_TOPIC, truck.getId(), event);
    }
}