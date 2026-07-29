package com.damianos.fleet.vehicletracking.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.damianos.fleet.vehicletracking.model.TruckEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ElasticSearchService {

    private final ElasticsearchClient client;

    public ElasticSearchService(ElasticsearchClient client) {
        this.client = client;
    }


    public void saveEvent(TruckEvent event) {

        System.out.println("ELASTIC SAVE TIME = " + event.getCreatedAt());

        try {


            client.index(i -> i
                    .index("truck-events")
                    .document(Map.of(
                            "truckId", event.getTruckId(),
                            "type", event.getType(),
                            "message", event.getMessage(),
                            "createdAt", event.getCreatedAt().toString()
                    ))
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}