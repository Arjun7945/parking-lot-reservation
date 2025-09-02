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
                .andExpect(jsonPath("$.totalCost").value(60.0));

        verify(reservationService, times(1)).createReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void createReservation_InvalidRequest_ReturnsBadRequest() throws Exception {
        reservationRequestDTO.setVehicleNumber("INVALID");

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
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"));

        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void getActiveReservations_ReturnsListOfReservations() throws Exception {
        List<ReservationResponseDTO> reservations = Arrays.asList(reservationResponseDTO);
        when(reservationService.getAllActiveReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].isActive").value(true));

        verify(reservationService, times(1)).getAllActiveReservations();
    }

    @Test
    void cancelReservation_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(reservationService).cancelReservation(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).cancelReservation(1L);
    }
}