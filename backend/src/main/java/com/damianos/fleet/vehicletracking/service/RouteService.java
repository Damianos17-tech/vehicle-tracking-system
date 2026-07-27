package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.OsrmResponse;
import com.damianos.fleet.vehicletracking.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;


@Service
public class RouteService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${osrm.url}")
    private String osrmUrl;

    public List<Position> generateRoute(Position start, Position end) {

        String url = osrmUrl +
                start.getLongitude() + "," + start.getLatitude() + ";" +
                end.getLongitude() + "," + end.getLatitude() +
                "?overview=full&geometries=geojson";

        OsrmResponse response =
                restTemplate.getForObject(url, OsrmResponse.class);

        List<Position> route = new ArrayList<>();

        if (response == null || response.routes() == null || response.routes().isEmpty()) {
            return route;
        }

        var coords = response.routes().get(0).geometry().coordinates();

        for (List<Double> point : coords) {
            double lng = point.get(0);
            double lat = point.get(1);

            route.add(new Position(lat, lng));
        }

        return route;
    }
}