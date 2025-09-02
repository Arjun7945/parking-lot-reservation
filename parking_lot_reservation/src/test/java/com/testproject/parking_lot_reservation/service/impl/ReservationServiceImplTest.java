package com.testproject.parking_lot_reservation.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.entity.Reservation;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.entity.VehicleType;
import com.testproject.parking_lot_reservation.exception.ResourceNotFoundException;
import com.testproject.parking_lot_reservation.repository.ReservationRepository;
import com.testproject.parking_lot_reservation.repository.SlotRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationRequestDTO reservationRequestDTO;
    private Slot slot;
    private Reservation activeReservation;
    private Reservation cancelledReservation;
    private ReservationResponseDTO reservationResponseDTO;

    @BeforeEach
    void setUp() {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(2L);
        vehicleType.setType("4 wheeler");
        vehicleType.setHourlyRate(30.0);

        Floor floor = new Floor();
        floor.setId(1L);
        floor.setFloorNumber(1);

        slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A-101");
        slot.setFloor(floor);
        slot.setVehicleType(vehicleType);
        slot.setIsAvailable(true);

        reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setSlotId(1L);
        reservationRequestDTO.setVehicleNumber("KA01AB1234");
        reservationRequestDTO.setStartTime(LocalDateTime.now().plusHours(1));
        reservationRequestDTO.setEndTime(LocalDateTime.now().plusHours(3));

        activeReservation = new Reservation();
        activeReservation.setId(1L);
        activeReservation.setSlot(slot);
        activeReservation.setVehicleNumber("KA01AB1234");
        activeReservation.setStartTime(LocalDateTime.now().plusHours(1));
        activeReservation.setEndTime(LocalDateTime.now().plusHours(3));
        activeReservation.setTotalCost(60.0);
        activeReservation.setDurationHours(2);
        activeReservation.setIsActive(true);
        activeReservation.setCreatedAt(LocalDateTime.now());

        cancelledReservation = new Reservation();
        cancelledReservation.setId(2L);
        cancelledReservation.setSlot(slot);
        cancelledReservation.setVehicleNumber("KA01CD5678");
        cancelledReservation.setStartTime(LocalDateTime.now().plusHours(1));
        cancelledReservation.setEndTime(LocalDateTime.now().plusHours(3));
        cancelledReservation.setTotalCost(60.0);
        cancelledReservation.setDurationHours(2);
        cancelledReservation.setIsActive(false);
        cancelledReservation.setCreatedAt(LocalDateTime.now());

        reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(1L);
        reservationResponseDTO.setSlotId(1L);
        reservationResponseDTO.setSlotNumber("A-101");
        reservationResponseDTO.setFloorNumber(1);
        reservationResponseDTO.setVehicleNumber("KA01AB1234");
        reservationResponseDTO.setStartTime(LocalDateTime.now().plusHours(1));
        reservationResponseDTO.setEndTime(LocalDateTime.now().plusHours(3));
        reservationResponseDTO.setTotalCost(60.0);
        reservationResponseDTO.setDurationHours(2);
        reservationResponseDTO.setIsActive(true);
        reservationResponseDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createReservation_ValidRequest_ReturnsReservation() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.findVehicleReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(activeReservation);

        ReservationResponseDTO result = reservationService.createReservation(reservationRequestDTO);

        assertNotNull(result);
        assertEquals("KA01AB1234", result.getVehicleNumber());
        assertEquals(60.0, result.getTotalCost());
        assertEquals(true, result.getIsActive());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_SlotNotFound_ThrowsException() {
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(reservationRequestDTO));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void getReservationById_ActiveReservation_ReturnsReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(activeReservation));

        ReservationResponseDTO result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(true, result.getIsActive());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void getReservationById_CancelledReservation_ReturnsReservation() {
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(cancelledReservation));

        ReservationResponseDTO result = reservationService.getReservationById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(false, result.getIsActive());
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    void getReservationById_NonExistingId_ThrowsException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(99L));
    }

    @Test
    void cancelReservation_ExistingActiveReservation_CancelsReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(activeReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(activeReservation);

        assertDoesNotThrow(() -> reservationService.cancelReservation(1L));
        assertFalse(activeReservation.getIsActive());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void getAllActiveReservations_ReturnsActiveReservations() {
        when(reservationRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(activeReservation));

        List<ReservationResponseDTO> result = reservationService.getAllActiveReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).getIsActive());
        verify(reservationRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void getAllReservations_ReturnsAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(activeReservation, cancelledReservation));

        List<ReservationResponseDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(2, result.size());
        // Should include both active and cancelled reservations
        verify(reservationRepository, times(1)).findAll();
    }
}