package com.testproject.parking_lot_reservation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.FloorResponseDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.exception.ResourceNotFoundException;
import com.testproject.parking_lot_reservation.repository.FloorRepository;
import com.testproject.parking_lot_reservation.service.FloorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {
    
    private final FloorRepository floorRepository;
    
    @Override
    public FloorResponseDTO createFloor(FloorRequestDTO requestDTO) {
        if (floorRepository.existsByFloorNumber(requestDTO.getFloorNumber())) {
            throw new ParkingException("Floor with number " + requestDTO.getFloorNumber() + " already exists");
        }
        
        Floor floor = new Floor();
        floor.setFloorNumber(requestDTO.getFloorNumber());
        floor.setName(requestDTO.getName());
        floor.setTotalSlots(requestDTO.getTotalSlots());
        
        Floor savedFloor = floorRepository.save(floor);
        return convertToDTO(savedFloor);
    }
    
    @Override
    public FloorResponseDTO getFloorById(Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
        return convertToDTO(floor);
    }
    
    @Override
    public FloorResponseDTO getFloorByNumber(Integer floorNumber) {
        Floor floor = floorRepository.findByFloorNumber(floorNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with number: " + floorNumber));
        return convertToDTO(floor);
    }
    
    @Override
    public List<FloorResponseDTO> getAllFloors() {
        return floorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteFloor(Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
        floorRepository.delete(floor);
    }
    
    private FloorResponseDTO convertToDTO(Floor floor) {
        FloorResponseDTO dto = new FloorResponseDTO();
        dto.setId(floor.getId());
        dto.setFloorNumber(floor.getFloorNumber());
        dto.setName(floor.getName());
        dto.setTotalSlots(floor.getTotalSlots());
        dto.setAvailableSlots(floor.getTotalSlots());
        dto.setCreatedAt(floor.getCreatedAt());
        dto.setUpdatedAt(floor.getUpdatedAt());
        return dto;
    }
}