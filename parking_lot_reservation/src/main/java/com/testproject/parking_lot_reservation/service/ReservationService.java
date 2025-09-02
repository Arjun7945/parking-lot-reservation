package com.testproject.parking_lot_reservation.service;

import java.util.List;

import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;

public interface ReservationService {
    ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO getReservationById(Long id);
    void cancelReservation(Long id);
    List<ReservationResponseDTO> getAllActiveReservations();
    
    // Add method to get all reservations
    List<ReservationResponseDTO> getAllReservations();
}