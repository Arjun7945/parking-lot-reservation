package com.testproject.parking_lot_reservation.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationResponseDTO {
    private Long id;
    private Long slotId;
    private String vehicleNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double cost;
}