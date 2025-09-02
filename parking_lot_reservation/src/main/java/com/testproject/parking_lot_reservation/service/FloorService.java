package com.testproject.parking_lot_reservation.service;

import java.util.List;

import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.FloorResponseDTO;

public interface FloorService {
    FloorResponseDTO createFloor(FloorRequestDTO floorRequestDTO);
    FloorResponseDTO getFloorById(Long id);
    List<FloorResponseDTO> getAllFloors();
    void deleteFloor(Long id);
    FloorResponseDTO getFloorByNumber(Integer floorNumber);
}