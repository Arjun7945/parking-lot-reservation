package com.testproject.parking_lot_reservation.service;

import java.util.List;

import com.testproject.parking_lot_reservation.dto.AvailabilityRequestDTO;
import com.testproject.parking_lot_reservation.dto.AvailabilityResponseDTO;

public interface ParkingService {
    List<AvailabilityResponseDTO> checkAvailability(AvailabilityRequestDTO availabilityRequestDTO);
}