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
            KafkaTemplate<String, Truck> truckKafkaTemplate,
            KafkaTemplate<String, TruckEvent> eventKafkaTemplate
    ) {
        this.truckKafkaTemplate = truckKafkaTemplate;
        this.eventKafkaTemplate = eventKafkaTemplate;
    }


    public void send(Truck truck) {

        truckKafkaTemplate.send(
                TRUCK_TOPIC,
                truck.getId(),
                truck
        );
    }


    public void sendEvent(Truck truck, String type, String message) {

        TruckEvent event = new TruckEvent(
                truck.getId(),
                type,
                message,
                Instant.now()
        );

        eventKafkaTemplate.send(
                EVENT_TOPIC,
                truck.getId(),
                event
        );
    }
}