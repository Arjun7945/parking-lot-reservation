package com.testproject.parking_lot_reservation.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.service.ParkingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/floors")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody FloorRequestDTO request) {
        return new ResponseEntity<>(parkingService.createFloor(request), HttpStatus.CREATED);
    }

    @PostMapping("/slots")
    public ResponseEntity<Slot> createSlot(@Valid @RequestBody SlotRequestDTO request) {
        return new ResponseEntity<>(parkingService.createSlot(request), HttpStatus.CREATED);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponseDTO> reserveSlot(@Valid @RequestBody ReservationRequestDTO request) {
        return new ResponseEntity<>(parkingService.reserveSlot(request), HttpStatus.CREATED);
    }

    @GetMapping("/availability")
    public ResponseEntity<Page<Slot>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            Pageable pageable) {
        return ResponseEntity.ok(parkingService.getAvailableSlots(startTime, endTime, pageable));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getReservation(id));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        parkingService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}