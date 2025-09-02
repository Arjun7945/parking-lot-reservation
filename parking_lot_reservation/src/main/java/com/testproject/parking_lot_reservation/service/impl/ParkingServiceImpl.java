package com.testproject.parking_lot_reservation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.testproject.parking_lot_reservation.dto.AvailabilityRequestDTO;
import com.testproject.parking_lot_reservation.dto.AvailabilityResponseDTO;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.repository.ReservationRepository;
import com.testproject.parking_lot_reservation.service.ParkingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    
    private final ReservationRepository reservationRepository;
    
    @Override
    public List<AvailabilityResponseDTO> checkAvailability(AvailabilityRequestDTO requestDTO) {
        validateAvailabilityRequest(requestDTO);
        
        List<Slot> availableSlots = reservationRepository.findAvailableSlots(
                requestDTO.getStartTime(),
                requestDTO.getEndTime(),
                requestDTO.getVehicleTypeId()
        );
        
        return availableSlots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private void validateAvailabilityRequest(AvailabilityRequestDTO requestDTO) {
        if (requestDTO.getStartTime().isAfter(requestDTO.getEndTime())) {
            throw new ParkingException("Start time must be before end time");
        }
    }
    
    private AvailabilityResponseDTO convertToDTO(Slot slot) {
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        dto.setSlotId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setFloorNumber(slot.getFloor().getFloorNumber());
        dto.setVehicleType(slot.getVehicleType().getType());
        dto.setHourlyRate(slot.getVehicleType().getHourlyRate());
        return dto;
    }
}