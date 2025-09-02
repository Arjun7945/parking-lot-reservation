package com.testproject.parking_lot_reservation.dto;

import lombok.Data;

@Data
public class AvailabilityResponseDTO {
    private Long slotId;
    private String slotNumber;
    private Integer floorNumber;
    private String vehicleType;
    private Double hourlyRate;
}