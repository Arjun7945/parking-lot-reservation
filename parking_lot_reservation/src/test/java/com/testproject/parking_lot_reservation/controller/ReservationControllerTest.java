package com.testproject.parking_lot_reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    private ReservationRequestDTO reservationRequestDTO;
    private ReservationResponseDTO reservationResponseDTO;
    private ReservationResponseDTO cancelledReservationResponseDTO;

    @BeforeEach
    void setUp() {
        reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setSlotId(1L);
        reservationRequestDTO.setVehicleNumber("KA01AB1234");
        reservationRequestDTO.setStartTime(LocalDateTime.now().plusHours(1));
        reservationRequestDTO.setEndTime(LocalDateTime.now().plusHours(3));

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

        cancelledReservationResponseDTO = new ReservationResponseDTO();
        cancelledReservationResponseDTO.setId(2L);
        cancelledReservationResponseDTO.setSlotId(1L);
        cancelledReservationResponseDTO.setSlotNumber("A-101");
        cancelledReservationResponseDTO.setFloorNumber(1);
        cancelledReservationResponseDTO.setVehicleNumber("KA01CD5678");
        cancelledReservationResponseDTO.setStartTime(LocalDateTime.now().plusHours(1));
        cancelledReservationResponseDTO.setEndTime(LocalDateTime.now().plusHours(3));
        cancelledReservationResponseDTO.setTotalCost(60.0);
        cancelledReservationResponseDTO.setDurationHours(2);
        cancelledReservationResponseDTO.setIsActive(false);
        cancelledReservationResponseDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createReservation_ValidRequest_ReturnsCreatedReservation() throws Exception {
        when(reservationService.createReservation(any(ReservationRequestDTO.class))).thenReturn(reservationResponseDTO);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"))
                .andExpect(jsonPath("$.totalCost").value(60.0))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(reservationService, times(1)).createReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void createReservation_InvalidVehicleNumber_ReturnsBadRequest() throws Exception {
        reservationRequestDTO.setVehicleNumber("INVALID");

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(reservationService, never()).createReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void createReservation_PastStartTime_ReturnsBadRequest() throws Exception {
        reservationRequestDTO.setStartTime(LocalDateTime.now().minusHours(1));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(reservationService, never()).createReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void getReservation_ExistingId_ReturnsReservation() throws Exception {
        when(reservationService.getReservationById(1L)).thenReturn(reservationResponseDTO);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void getReservation_CancelledReservation_ReturnsReservation() throws Exception {
        when(reservationService.getReservationById(2L)).thenReturn(cancelledReservationResponseDTO);

        mockMvc.perform(get("/api/reservations/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.isActive").value(false));

        verify(reservationService, times(1)).getReservationById(2L);
    }

    @Test
    void getActiveReservations_ReturnsListOfActiveReservations() throws Exception {
        List<ReservationResponseDTO> activeReservations = Arrays.asList(reservationResponseDTO);
        when(reservationService.getAllActiveReservations()).thenReturn(activeReservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].isActive").value(true));

        verify(reservationService, times(1)).getAllActiveReservations();
    }

    @Test
    void getAllReservations_ReturnsListOfAllReservations() throws Exception {
        List<ReservationResponseDTO> allReservations = Arrays.asList(reservationResponseDTO, cancelledReservationResponseDTO);
        when(reservationService.getAllReservations()).thenReturn(allReservations);

        mockMvc.perform(get("/api/reservations/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].isActive").value(false));

        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void cancelReservation_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(reservationService).cancelReservation(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).cancelReservation(1L);
    }
}