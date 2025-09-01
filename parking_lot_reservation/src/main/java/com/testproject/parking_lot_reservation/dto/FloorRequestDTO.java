package com.testproject.parking_lot_reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FloorRequestDTO {
    @NotBlank(message = "Floor number is required")
    private String floorNumber;
}