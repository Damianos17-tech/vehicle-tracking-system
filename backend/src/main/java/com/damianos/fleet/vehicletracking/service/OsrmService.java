package com.damianos.fleet.vehicletracking.service;

import com.damianos.fleet.vehicletracking.model.OsrmResponse;
import com.damianos.fleet.vehicletracking.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OsrmService {

    @Value("${osrm.host}")
    private String osrmHost;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Position> getRoute(double startLat, double startLon,
                                   double endLat, double endLon) {

        String url = buildUrl(startLat, startLon, endLat, endLon);

        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);

        if (response == null ||
                response.routes() == null ||
                response.routes().isEmpty()) {
            throw new RuntimeException("No route from OSRM");
        }

        List<List<Double>> coords =
                response.routes().get(0).geometry().coordinates();

        return coords.stream()
                .map(c -> new Position(
                        c.get(1), // lat
                        c.get(0)  // lon
                ))
                .toList();
    }

    private String buildUrl(double startLat, double startLon,
                            double endLat, double endLon) {

        return osrmHost + "/route/v1/driving/" +
                startLon + "," + startLat + ";" + endLon + "," + endLat + "?overview=full&geometries=geojson";
    }






    private Position mapToPosition(List<Double> c) {
        return new Position(c.get(1), c.get(0));
    }
}