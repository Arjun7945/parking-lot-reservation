package com.testproject.parking_lot_reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SlotRequestDTO {
    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Floor ID is required")
    private Long floorId;

    @NotNull(message = "Vehicle type ID is required")
    private Long vehicleTypeId;
}