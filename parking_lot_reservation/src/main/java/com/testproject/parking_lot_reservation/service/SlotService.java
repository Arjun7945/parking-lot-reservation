package com.testproject.parking_lot_reservation.service;

import java.util.List;

import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.dto.SlotResponseDTO;

public interface SlotService {
    SlotResponseDTO createSlot(SlotRequestDTO slotRequestDTO);
    SlotResponseDTO getSlotById(Long id);
    List<SlotResponseDTO> getAllSlots();
    List<SlotResponseDTO> getSlotsByFloor(Long floorId);
    List<SlotResponseDTO> getSlotsByVehicleType(Long vehicleTypeId);
    void deleteSlot(Long id);
}