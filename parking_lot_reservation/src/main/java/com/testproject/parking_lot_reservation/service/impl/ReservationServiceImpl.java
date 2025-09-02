package com.testproject.parking_lot_reservation.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.entity.Reservation;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.exception.ResourceNotFoundException;
import com.testproject.parking_lot_reservation.repository.ReservationRepository;
import com.testproject.parking_lot_reservation.repository.SlotRepository;
import com.testproject.parking_lot_reservation.service.ReservationService;
import com.testproject.parking_lot_reservation.util.VehicleNumberValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    
    @Override
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        validateReservationRequest(requestDTO);
        
        Slot slot = slotRepository.findById(requestDTO.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + requestDTO.getSlotId()));
        
        if (!slot.getIsAvailable()) {
            throw new ParkingException("Slot is not available for reservation");
        }
        
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                requestDTO.getSlotId(), requestDTO.getStartTime(), requestDTO.getEndTime());
        
        if (!conflictingReservations.isEmpty()) {
            throw new ParkingException("Slot is already reserved for the requested time period");
        }
        
        List<Reservation> vehicleReservations = reservationRepository.findVehicleReservations(
                requestDTO.getVehicleNumber(), requestDTO.getStartTime(), requestDTO.getEndTime());
        
        if (!vehicleReservations.isEmpty()) {
            throw new ParkingException("Vehicle already has a reservation during this time period");
        }
        
        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setVehicleNumber(requestDTO.getVehicleNumber());
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        
        double totalCost = calculateCost(slot, requestDTO.getStartTime(), requestDTO.getEndTime());
        reservation.setTotalCost(totalCost);
        
        Duration duration = Duration.between(requestDTO.getStartTime(), requestDTO.getEndTime());
        long minutes = duration.toMinutes();
        int hours = (int) Math.ceil(minutes / 60.0);
        reservation.setDurationHours(hours);
        
        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }
    
    @Override
    public ReservationResponseDTO getReservationById(Long id) {
        // Change to find by ID regardless of active status
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return convertToDTO(reservation);
    }
    
    @Override
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (!reservation.getIsActive()) {
            throw new ParkingException("Reservation is already cancelled");
        }
        
        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ParkingException("Cannot cancel reservation that has already started");
        }
        
        reservation.setIsActive(false);
        reservationRepository.save(reservation);
    }
    
    @Override
    public List<ReservationResponseDTO> getAllActiveReservations() {
        return reservationRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Add method to get all reservations (including cancelled)
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private void validateReservationRequest(ReservationRequestDTO requestDTO) {
        if (requestDTO.getStartTime().isAfter(requestDTO.getEndTime())) {
            throw new ParkingException("Start time must be before end time");
        }
        
        Duration duration = Duration.between(requestDTO.getStartTime(), requestDTO.getEndTime());
        if (duration.toHours() > 24) {
            throw new ParkingException("Reservation duration cannot exceed 24 hours");
        }
        
        if (!VehicleNumberValidator.isValid(requestDTO.getVehicleNumber())) {
            throw new ParkingException("Vehicle number must be in format XX00XX0000");
        }
    }
    
    private double calculateCost(Slot slot, LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        int hours = (int) Math.ceil(minutes / 60.0);
        
        return hours * slot.getVehicleType().getHourlyRate();
    }
    
    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setSlotId(reservation.getSlot().getId());
        dto.setSlotNumber(reservation.getSlot().getSlotNumber());
        dto.setFloorNumber(reservation.getSlot().getFloor().getFloorNumber());
        dto.setVehicleNumber(reservation.getVehicleNumber());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setTotalCost(reservation.getTotalCost());
        dto.setDurationHours(reservation.getDurationHours());
        dto.setIsActive(reservation.getIsActive());
        dto.setCreatedAt(reservation.getCreatedAt());
        return dto;
    }
}