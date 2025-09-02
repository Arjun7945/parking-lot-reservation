package com.testproject.parking_lot_reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FloorRequestDTO {
    @NotNull(message = "Floor number is required")
    @Positive(message = "Floor number must be positive")
    private Integer floorNumber;
    
    @NotBlank(message = "Floor name is required")
    private String name;
    
    @NotNull(message = "Total slots is required")
    @Positive(message = "Total slots must be positive")
    private Integer totalSlots;
}