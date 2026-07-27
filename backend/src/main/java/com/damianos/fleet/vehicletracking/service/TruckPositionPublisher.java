package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.Truck;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckPositionPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public TruckPositionPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(Truck truck) {
        messagingTemplate.convertAndSend("/topic/trucks", truck);
    }
}