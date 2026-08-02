package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.Truck;
import com.damianos.fleet.vehicletracking.model.TruckEvent;
import com.damianos.fleet.vehicletracking.repository.JpaTruckEventRepository;
import com.damianos.fleet.vehicletracking.repository.JpaTruckRepository;
import com.damianos.fleet.vehicletracking.repository.TruckEventPublisher;
import com.damianos.fleet.vehicletracking.repository.TruckRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DatabaseSyncService {

    private final JpaTruckRepository jpaTruckRepository;
    private final TruckRepository truckRepository;
    private final JpaTruckEventRepository jpaTruckEventRepository;
    private final TruckEventPublisher eventPublisher;



    public DatabaseSyncService(JpaTruckRepository jpaTruckRepository, TruckRepository truckRepository,
            JpaTruckEventRepository jpaTruckEventRepository, TruckEventPublisher eventPublisher)
    {
        this.jpaTruckRepository = jpaTruckRepository;
        this.truckRepository = truckRepository;
        this.jpaTruckEventRepository = jpaTruckEventRepository;
        this.eventPublisher = eventPublisher;

    }


//    @Scheduled(fixedRate = 5000)
//    public void syncTrucks() {
//
//        List<Truck> trucks = jpaTruckRepository.findAll();
//
//        trucks.forEach(truckRepository::save);
//    }


    @Scheduled(fixedRate = 5000)
    public void syncTrucks() {


        List<Truck> trucks = truckRepository.findAll();

        trucks.sort(Comparator.comparing(Truck::getId));
        trucks.forEach(jpaTruckRepository::save);

    }


    @Scheduled(fixedRate = 5000)
    public void syncEvents() {

        List<TruckEvent> events = eventPublisher.peekEvents();

        try {
            jpaTruckEventRepository.saveAll(events);
            eventPublisher.removeEvents(events);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}