package com.damianos.fleet.simulator.model;



import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckEvent {

    private String type;
    private String message;

    private Instant createdAt;

    private Long id;

    private String truckId;


    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("Europe/Warsaw"));


    public TruckEvent(String truckId, String type, String message, Instant createdAt) {
        this.truckId = truckId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return type + " | "
                + message
                + ", "
                + FORMATTER.format(createdAt);
    }
}