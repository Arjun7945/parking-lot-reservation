package com.testproject.parking_lot_reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Management", description = "APIs for managing parking reservations")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    @PostMapping
    @Operation(summary = "Create a new reservation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
        @ApiResponse(responseCode = "404", description = "Slot not found"),
        @ApiResponse(responseCode = "409", description = "Time conflict or slot not available")
    })
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO response = reservationService.createReservation(reservationRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID (including cancelled)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservation found"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        ReservationResponseDTO response = reservationService.getReservationById(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a reservation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reservation cancelled successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot cancel started reservation"),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "409", description = "Reservation already cancelled")
    })
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all active reservations")
    @ApiResponse(responseCode = "200", description = "List of active reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservations() {
        List<ReservationResponseDTO> responses = reservationService.getAllActiveReservations();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/all")
    @Operation(summary = "Get all reservations (including cancelled)")
    @ApiResponse(responseCode = "200", description = "List of all reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> responses = reservationService.getAllReservations();
        return ResponseEntity.ok(responses);
    }
}