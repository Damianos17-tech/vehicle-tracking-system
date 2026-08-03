package com.damianos.fleet.vehicletracking.kafka;

import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckEvent;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import com.damianos.fleet.vehicletracking.service.ElasticSearchService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


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

            existing.setPosition(truck.getPosition());
            existing.setSpeed(truck.getSpeed());
            existing.setFuelLevel(truck.getFuelLevel());
            existing.setStatus(truck.getStatus());

            existing.setTechnicalCondition(
                    truck.getTechnicalCondition()
            );

            existing.setTotalDistanceKm(
                    truck.getTotalDistanceKm()
            );

            existing.setDistanceSinceServiceKm(
                    truck.getDistanceSinceServiceKm()
            );

            existing.setKmToService(
                    truck.getKmToService()
            );


            truckRepository.save(existing);


            messagingTemplate.convertAndSend(
                    "/topic/trucks",
                    existing
            );

            return;
        }


        // FRONTEND
        messagingTemplate.convertAndSend(
                "/topic/trucks",
                truck
        );


        // Elasticsearch później
        // elasticSearchService.saveTruck(truck);
    }
}