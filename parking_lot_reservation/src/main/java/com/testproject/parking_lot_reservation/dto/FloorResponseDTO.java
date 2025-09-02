package com.testproject.parking_lot_reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FloorResponseDTO {
    private Long id;
    private Integer floorNumber;
    private String name;
    private Integer totalSlots;
    private Integer availableSlots;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}