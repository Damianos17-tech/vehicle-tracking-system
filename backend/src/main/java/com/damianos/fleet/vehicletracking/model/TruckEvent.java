package com.damianos.fleet.vehicletracking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;

@Entity
@Table(name = "truck_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckEvent {

    private String type;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private boolean saved = false;
    private String truckId;


    public enum TruckEventType {
        MOVING,
        STOPPED,
        BREAKDOWN,
        ACCIDENT,
        LOW_FUEL,
        SERVICE_REQUIRED,
        GPS_LOST,
        DRIVER_BREAK
    }

    @Override
    public String toString() {
        return type + " | "
                + message
                + ", "
                + createdAt
                .atZone(ZoneId.of("Europe/Warsaw"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public TruckEvent(String truckId, String type, String message, Instant  createdAt) {
        this.truckId = truckId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
    }

}