package com.testproject.parking_lot_reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SlotResponseDTO {
    private Long id;
    private String slotNumber;
    private Long floorId;
    private Integer floorNumber;
    private Long vehicleTypeId;
    private String vehicleType;
    private Double hourlyRate;
    private Boolean isAvailable;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}