package com.damianos.fleet.simulator.model;

import java.util.List;

public record OsrmResponse(List<Route> routes) {

    public record Route(Geometry geometry) {}

    public record Geometry(List<List<Double>> coordinates) {}
}