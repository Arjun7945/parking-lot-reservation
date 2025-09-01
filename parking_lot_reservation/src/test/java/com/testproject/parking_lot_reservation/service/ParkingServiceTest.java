package com.testproject.parking_lot_reservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.entity.Reservation;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.entity.VehicleType;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.repository.FloorRepository;
import com.testproject.parking_lot_reservation.repository.ReservationRepository;
import com.testproject.parking_lot_reservation.repository.SlotRepository;
import com.testproject.parking_lot_reservation.repository.VehicleTypeRepository;

class ParkingServiceTest {

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private FloorRepository floorRepository;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    private ReservationRequestDTO request;
    private Slot slot;
    private VehicleType vehicleType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new ReservationRequestDTO();
        request.setSlotId(1L);
        request.setVehicleNumber("KA05MH1234");
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(2));

        vehicleType = new VehicleType();
        vehicleType.setId(1L);
        vehicleType.setTypeName("4_WHEELER");
        vehicleType.setHourlyRate(30.0);

        slot = new Slot();
        slot.setId(1L);
        slot.setVehicleType(vehicleType);
    }

    @Test
    void testReserveSlot_Success() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> {
            Reservation r = i.getArgument(0);
            r.setId(1L);
            return r;
        });

        ReservationResponseDTO response = parkingService.reserveSlot(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(60.0, response.getCost()); // 2 hours * 30
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testReserveSlot_SlotNotFound() {
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ParkingException.class, () -> parkingService.reserveSlot(request));
    }

    @Test
    void testReserveSlot_OverlappingReservation() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
                .thenReturn(Collections.singletonList(new Reservation()));

        assertThrows(ParkingException.class, () -> parkingService.reserveSlot(request));
    }

    @Test
    void testReserveSlot_InvalidTimeRange() {
        request.setEndTime(request.getStartTime().minusHours(1));

        assertThrows(ParkingException.class, () -> parkingService.reserveSlot(request));
    }

    @Test
    void testReserveSlot_ExcessiveDuration() {
        request.setEndTime(request.getStartTime().plusHours(25));

        assertThrows(ParkingException.class, () -> parkingService.reserveSlot(request));
    }
}