package com.damianos.fleet.vehicletracking.controller;

import com.damianos.fleet.vehicletracking.model.Position;
import com.damianos.fleet.vehicletracking.service.OsrmService;
import com.damianos.fleet.vehicletracking.service.TruckPositionPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final OsrmService osrmService;
    private final TruckPositionPublisher publisher;

    public TestController(OsrmService osrmService, TruckPositionPublisher publisher) {
        this.osrmService = osrmService;
        this.publisher = publisher;
    }

    @GetMapping("/route")
    public List<Position> testRoute() {

        return osrmService.getRoute(
                52.2297, 21.0122,   // Pałac Kultury
                52.2350, 21.0200    // kilka ulic dalej
        );
    }

    @PostMapping("/test-send")
    public void test() {
        //publisher.send("TRUCK-001", new Position(52.2297, 21.0122),500);
    }

}