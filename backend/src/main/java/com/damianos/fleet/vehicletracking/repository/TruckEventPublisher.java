package com.damianos.fleet.vehicletracking.repository;

import com.damianos.fleet.vehicletracking.model.TruckEvent;
import com.damianos.fleet.vehicletracking.service.ElasticSearchService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.damianos.fleet.vehicletracking.model.Truck;

@Service
public class TruckEventPublisher {

    private final Queue<TruckEvent> pendingDatabaseEvents = new ConcurrentLinkedQueue<>();
    private final ElasticSearchService elasticSearchService;


    public TruckEventPublisher(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }


    public void publish(Truck truck, String type, String message) {

        TruckEvent event = new TruckEvent(
                truck.getId(),
                type,
                message,
                Instant.now()
        );


        // frontend
        truck.addEvent(event);


        // baza danych
        pendingDatabaseEvents.add(event);


        // Elasticsearch
        //elasticSearchService.saveEvent(event);
    }


    public List<TruckEvent> drainEvents() {

        List<TruckEvent> events = new ArrayList<>();

        TruckEvent event;

        while ((event = pendingDatabaseEvents.poll()) != null) {
            events.add(event);
        }

        return events;
    }


    public List<TruckEvent> peekEvents() {

        return new ArrayList<>(pendingDatabaseEvents);
    }


    public void removeEvents(List<TruckEvent> events) {

        pendingDatabaseEvents.removeAll(events);
    }
}