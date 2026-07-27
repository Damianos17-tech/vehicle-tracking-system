package com.damianos.fleet.vehicletracking.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class FleetStats {
    public long total;
    public long broken;
    public long warnings;
    public long stopped;
    public long paused;

}