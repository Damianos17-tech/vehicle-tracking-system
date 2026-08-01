package com.damianos.fleet.vehicletracking.kafka;

import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import com.damianos.fleet.vehicletracking.service.ElasticSearchService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class TruckStateConsumer {


    private final TruckRepository truckRepository;
    private final ElasticSearchService elasticSearchService;
    private final SimpMessagingTemplate messagingTemplate;


    public TruckStateConsumer(
            TruckRepository truckRepository,
            ElasticSearchService elasticSearchService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.truckRepository = truckRepository;
        this.elasticSearchService = elasticSearchService;
        this.messagingTemplate = messagingTemplate;
    }


    @KafkaListener(
            topics = "truck-state",
            groupId = "fleet-backend"
    )
    public void consume(Truck truck) {


        //System.out.println("KAFKA RECEIVED: " + truck.getId());


        // RAM
        Truck existing = truckRepository.findById(truck.getId());

        if (existing != null) {

            truck.setEvents(existing.getEvents());
            truck.setWarnings(existing.getWarnings());
            truck.setFailures(existing.getFailures());

            if (truck.getRoute() == null) {
                truck.setRoute(existing.getRoute());
            }
        }

        truckRepository.save(truck);


        // FRONTEND
        messagingTemplate.convertAndSend(
                "/topic/trucks",
                truck
        );


        // Elasticsearch później
        // elasticSearchService.saveTruck(truck);
    }
}