package com.damianos.fleet.vehicletracking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class InfrastructureStats {

    String status;
    double cpu;
    double ram;
    double disk;
    long uptime;
}