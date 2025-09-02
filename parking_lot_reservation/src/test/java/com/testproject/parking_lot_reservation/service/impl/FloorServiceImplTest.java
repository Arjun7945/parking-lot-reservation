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

import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.FloorResponseDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.exception.ResourceNotFoundException;
import com.testproject.parking_lot_reservation.repository.FloorRepository;

@ExtendWith(MockitoExtension.class)
class FloorServiceImplTest {

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private FloorServiceImpl floorService;

    private FloorRequestDTO floorRequestDTO;
    private Floor floor;
    private FloorResponseDTO floorResponseDTO;

    @BeforeEach
    void setUp() {
        floorRequestDTO = new FloorRequestDTO();
        floorRequestDTO.setFloorNumber(1);
        floorRequestDTO.setName("Ground Floor");
        floorRequestDTO.setTotalSlots(50);

        floor = new Floor();
        floor.setId(1L);
        floor.setFloorNumber(1);
        floor.setName("Ground Floor");
        floor.setTotalSlots(50);
        floor.setCreatedAt(LocalDateTime.now());
        floor.setUpdatedAt(LocalDateTime.now());

        floorResponseDTO = new FloorResponseDTO();
        floorResponseDTO.setId(1L);
        floorResponseDTO.setFloorNumber(1);
        floorResponseDTO.setName("Ground Floor");
        floorResponseDTO.setTotalSlots(50);
        floorResponseDTO.setAvailableSlots(50);
        floorResponseDTO.setCreatedAt(LocalDateTime.now());
        floorResponseDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createFloor_ValidRequest_ReturnsFloorResponse() {
        when(floorRepository.existsByFloorNumber(1)).thenReturn(false);
        when(floorRepository.save(any(Floor.class))).thenReturn(floor);

        FloorResponseDTO result = floorService.createFloor(floorRequestDTO);

        assertNotNull(result);
        assertEquals(1, result.getFloorNumber());
        assertEquals("Ground Floor", result.getName());
        verify(floorRepository, times(1)).save(any(Floor.class));
    }

    @Test
    void createFloor_DuplicateFloorNumber_ThrowsException() {
        when(floorRepository.existsByFloorNumber(1)).thenReturn(true);

        assertThrows(ParkingException.class, () -> floorService.createFloor(floorRequestDTO));
        verify(floorRepository, never()).save(any(Floor.class));
    }

    @Test
    void getFloorById_ExistingId_ReturnsFloor() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));

        FloorResponseDTO result = floorService.getFloorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(floorRepository, times(1)).findById(1L);
    }

    @Test
    void getFloorById_NonExistingId_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> floorService.getFloorById(1L));
    }

    @Test
    void getAllFloors_ReturnsListOfFloors() {
        when(floorRepository.findAll()).thenReturn(Arrays.asList(floor));

        List<FloorResponseDTO> result = floorService.getAllFloors();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(floorRepository, times(1)).findAll();
    }

    @Test
    void deleteFloor_ExistingId_DeletesFloor() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        doNothing().when(floorRepository).delete(floor);

        assertDoesNotThrow(() -> floorService.deleteFloor(1L));
        verify(floorRepository, times(1)).delete(floor);
    }

    @Test
    void deleteFloor_NonExistingId_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> floorService.deleteFloor(1L));
        verify(floorRepository, never()).delete(any(Floor.class));
    }
}