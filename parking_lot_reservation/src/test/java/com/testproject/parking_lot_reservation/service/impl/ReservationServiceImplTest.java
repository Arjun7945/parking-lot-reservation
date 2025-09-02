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
import com.testproject.parking_lot_reservation.exception.ParkingException;
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
    private Reservation reservation;
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

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setSlot(slot);
        reservation.setVehicleNumber("KA01AB1234");
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(3));
        reservation.setTotalCost(60.0);
        reservation.setDurationHours(2);
        reservation.setIsActive(true);
        reservation.setCreatedAt(LocalDateTime.now());

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
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationResponseDTO result = reservationService.createReservation(reservationRequestDTO);

        assertNotNull(result);
        assertEquals("KA01AB1234", result.getVehicleNumber());
        assertEquals(60.0, result.getTotalCost());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_SlotNotFound_ThrowsException() {
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(reservationRequestDTO));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void createReservation_SlotNotAvailable_ThrowsException() {
        slot.setIsAvailable(false);
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(ParkingException.class, () -> reservationService.createReservation(reservationRequestDTO));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void createReservation_TimeConflict_ThrowsException() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Arrays.asList(new Reservation()));

        assertThrows(ParkingException.class, () -> reservationService.createReservation(reservationRequestDTO));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void createReservation_InvalidVehicleNumber_ThrowsException() {
        reservationRequestDTO.setVehicleNumber("INVALID");

        assertThrows(ParkingException.class, () -> reservationService.createReservation(reservationRequestDTO));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void getReservationById_ExistingId_ReturnsReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationResponseDTO result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void cancelReservation_ExistingId_CancelsReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        assertDoesNotThrow(() -> reservationService.cancelReservation(1L));
        assertFalse(reservation.getIsActive());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void getAllActiveReservations_ReturnsActiveReservations() {
        when(reservationRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(reservation));

        List<ReservationResponseDTO> result = reservationService.getAllActiveReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByIsActiveTrue();
    }
}