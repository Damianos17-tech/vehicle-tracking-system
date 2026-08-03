package com.damianos.fleet.vehicletracking.kafka;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckEvent;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import com.damianos.fleet.vehicletracking.service.ElasticSearchService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class TruckEventConsumer {

    private final TruckRepository truckRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ElasticSearchService elasticSearchService;

    public TruckEventConsumer(
            TruckRepository truckRepository,
            SimpMessagingTemplate messagingTemplate, ElasticSearchService elasticSearchService
    ) {
        this.truckRepository = truckRepository;
        this.messagingTemplate = messagingTemplate;
        this.elasticSearchService = elasticSearchService;
    }


    @KafkaListener(
            topics = "truck-events",
            groupId = "fleet-events"
    )
    public void consume(TruckEvent event) {


        //System.out.println("EVENT FROM KAFKA: " + event.getTruckId() + " " + event.getType());


        Truck truck = truckRepository.findById(event.getTruckId());


        if (truck == null) {
            System.out.println("Truck not found: " + event.getTruckId());
            return;
        }


        // dodajemy event do trucka
        truck.addEvent(event);

        // zapisujemy zmianę
        truckRepository.save(truck);


        // wysyłamy do React
        messagingTemplate.convertAndSend(
                "/topic/trucks",
                truck
        );

        // wysyłamy do ElasticSearch
        elasticSearchService.saveEvent(event);



    }
}