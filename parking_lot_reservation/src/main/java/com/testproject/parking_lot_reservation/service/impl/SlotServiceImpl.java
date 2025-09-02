package com.testproject.parking_lot_reservation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.dto.SlotResponseDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.entity.VehicleType;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.exception.ResourceNotFoundException;
import com.testproject.parking_lot_reservation.repository.FloorRepository;
import com.testproject.parking_lot_reservation.repository.SlotRepository;
import com.testproject.parking_lot_reservation.repository.VehicleTypeRepository;
import com.testproject.parking_lot_reservation.service.SlotService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {
    
    private final SlotRepository slotRepository;
    private final FloorRepository floorRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    
    @Override
    public SlotResponseDTO createSlot(SlotRequestDTO requestDTO) {
        Floor floor = floorRepository.findById(requestDTO.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + requestDTO.getFloorId()));
        
        VehicleType vehicleType = vehicleTypeRepository.findById(requestDTO.getVehicleTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with id: " + requestDTO.getVehicleTypeId()));
        
        if (slotRepository.findBySlotNumberAndFloorId(requestDTO.getSlotNumber(), requestDTO.getFloorId()).isPresent()) {
            throw new ParkingException("Slot with number " + requestDTO.getSlotNumber() + " already exists on floor " + floor.getFloorNumber());
        }
        
        Slot slot = new Slot();
        slot.setSlotNumber(requestDTO.getSlotNumber());
        slot.setFloor(floor);
        slot.setVehicleType(vehicleType);
        slot.setIsAvailable(true);
        
        Slot savedSlot = slotRepository.save(slot);
        return convertToDTO(savedSlot);
    }
    
    @Override
    public SlotResponseDTO getSlotById(Long id) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + id));
        return convertToDTO(slot);
    }
    
    @Override
    public List<SlotResponseDTO> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SlotResponseDTO> getSlotsByFloor(Long floorId) {
        return slotRepository.findByFloorId(floorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SlotResponseDTO> getSlotsByVehicleType(Long vehicleTypeId) {
        return slotRepository.findByVehicleTypeId(vehicleTypeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteSlot(Long id) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + id));
        slotRepository.delete(slot);
    }
    
    private SlotResponseDTO convertToDTO(Slot slot) {
        SlotResponseDTO dto = new SlotResponseDTO();
        dto.setId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setFloorId(slot.getFloor().getId());
        dto.setFloorNumber(slot.getFloor().getFloorNumber());
        dto.setVehicleTypeId(slot.getVehicleType().getId());
        dto.setVehicleType(slot.getVehicleType().getType());
        dto.setHourlyRate(slot.getVehicleType().getHourlyRate());
        dto.setIsAvailable(slot.getIsAvailable());
        dto.setCreatedAt(slot.getCreatedAt());
        dto.setUpdatedAt(slot.getUpdatedAt());
        return dto;
    }
}