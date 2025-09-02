package com.testproject.parking_lot_reservation.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class SlotServiceImplTest {

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private FloorRepository floorRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @InjectMocks
    private SlotServiceImpl slotService;

    private SlotRequestDTO slotRequestDTO;
    private Floor floor;
    private VehicleType vehicleType;
    private Slot slot;
    private SlotResponseDTO slotResponseDTO;

    @BeforeEach
    void setUp() {
        slotRequestDTO = new SlotRequestDTO();
        slotRequestDTO.setSlotNumber("A-101");
        slotRequestDTO.setFloorId(1L);
        slotRequestDTO.setVehicleTypeId(2L);

        floor = new Floor();
        floor.setId(1L);
        floor.setFloorNumber(1);

        vehicleType = new VehicleType();
        vehicleType.setId(2L);
        vehicleType.setType("4 wheeler");
        vehicleType.setHourlyRate(30.0);

        slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A-101");
        slot.setFloor(floor);
        slot.setVehicleType(vehicleType);
        slot.setIsAvailable(true);
        slot.setCreatedAt(LocalDateTime.now());
        slot.setUpdatedAt(LocalDateTime.now());

        slotResponseDTO = new SlotResponseDTO();
        slotResponseDTO.setId(1L);
        slotResponseDTO.setSlotNumber("A-101");
        slotResponseDTO.setFloorId(1L);
        slotResponseDTO.setFloorNumber(1);
        slotResponseDTO.setVehicleTypeId(2L);
        slotResponseDTO.setVehicleType("4 wheeler");
        slotResponseDTO.setHourlyRate(30.0);
        slotResponseDTO.setIsAvailable(true);
        slotResponseDTO.setCreatedAt(LocalDateTime.now());
        slotResponseDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createSlot_ValidRequest_ReturnsSlotResponse() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(vehicleTypeRepository.findById(2L)).thenReturn(Optional.of(vehicleType));
        when(slotRepository.findBySlotNumberAndFloorId("A-101", 1L)).thenReturn(Optional.empty());
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        SlotResponseDTO result = slotService.createSlot(slotRequestDTO);

        assertNotNull(result);
        assertEquals("A-101", result.getSlotNumber());
        assertEquals("4 wheeler", result.getVehicleType());
        verify(slotRepository, times(1)).save(any(Slot.class));
    }

    @Test
    void createSlot_FloorNotFound_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slotService.createSlot(slotRequestDTO));
        verify(slotRepository, never()).save(any(Slot.class));
    }

    @Test
    void createSlot_VehicleTypeNotFound_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(vehicleTypeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slotService.createSlot(slotRequestDTO));
        verify(slotRepository, never()).save(any(Slot.class));
    }

    @Test
    void createSlot_DuplicateSlotNumber_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(vehicleTypeRepository.findById(2L)).thenReturn(Optional.of(vehicleType));
        when(slotRepository.findBySlotNumberAndFloorId("A-101", 1L)).thenReturn(Optional.of(new Slot()));

        assertThrows(ParkingException.class, () -> slotService.createSlot(slotRequestDTO));
        verify(slotRepository, never()).save(any(Slot.class));
    }

    @Test
    void getSlotById_ExistingId_ReturnsSlot() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        SlotResponseDTO result = slotService.getSlotById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(slotRepository, times(1)).findById(1L);
    }

    @Test
    void getAllSlots_ReturnsListOfSlots() {
        when(slotRepository.findAll()).thenReturn(Arrays.asList(slot));

        List<SlotResponseDTO> result = slotService.getAllSlots();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(slotRepository, times(1)).findAll();
    }

    @Test
    void getSlotsByFloor_ExistingFloorId_ReturnsSlots() {
        when(slotRepository.findByFloorId(1L)).thenReturn(Arrays.asList(slot));

        List<SlotResponseDTO> result = slotService.getSlotsByFloor(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(slotRepository, times(1)).findByFloorId(1L);
    }

    @Test
    void deleteSlot_ExistingId_DeletesSlot() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        doNothing().when(slotRepository).delete(slot);

        assertDoesNotThrow(() -> slotService.deleteSlot(1L));
        verify(slotRepository, times(1)).delete(slot);
    }
}