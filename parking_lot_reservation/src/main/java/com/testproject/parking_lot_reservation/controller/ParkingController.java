package com.testproject.parking_lot_reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testproject.parking_lot_reservation.dto.AvailabilityRequestDTO;
import com.testproject.parking_lot_reservation.dto.AvailabilityResponseDTO;
import com.testproject.parking_lot_reservation.service.ParkingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Parking Operations", description = "APIs for parking operations")
public class ParkingController {
    
    private final ParkingService parkingService;
    
    @PostMapping("/availability")
    @Operation(summary = "Check available slots for a given time range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available slots retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range")
    })
    public ResponseEntity<List<AvailabilityResponseDTO>> checkAvailability(
            @Valid @RequestBody AvailabilityRequestDTO availabilityRequestDTO) {
        List<AvailabilityResponseDTO> responses = parkingService.checkAvailability(availabilityRequestDTO);
        return ResponseEntity.ok(responses);
    }
}