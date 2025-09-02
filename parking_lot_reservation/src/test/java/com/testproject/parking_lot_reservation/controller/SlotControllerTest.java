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
import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.dto.SlotResponseDTO;
import com.testproject.parking_lot_reservation.service.SlotService;

@WebMvcTest(SlotController.class)
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SlotService slotService;

    private SlotRequestDTO slotRequestDTO;
    private SlotResponseDTO slotResponseDTO;

    @BeforeEach
    void setUp() {
        slotRequestDTO = new SlotRequestDTO();
        slotRequestDTO.setSlotNumber("A-101");
        slotRequestDTO.setFloorId(1L);
        slotRequestDTO.setVehicleTypeId(2L);

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
    void createSlot_ValidRequest_ReturnsCreatedSlot() throws Exception {
        when(slotService.createSlot(any(SlotRequestDTO.class))).thenReturn(slotResponseDTO);

        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotNumber").value("A-101"))
                .andExpect(jsonPath("$.vehicleType").value("4 wheeler"));

        verify(slotService, times(1)).createSlot(any(SlotRequestDTO.class));
    }

    @Test
    void createSlot_InvalidRequest_ReturnsBadRequest() throws Exception {
        slotRequestDTO.setSlotNumber(null);

        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(slotService, never()).createSlot(any(SlotRequestDTO.class));
    }

    @Test
    void getSlot_ExistingId_ReturnsSlot() throws Exception {
        when(slotService.getSlotById(1L)).thenReturn(slotResponseDTO);

        mockMvc.perform(get("/api/slots/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotNumber").value("A-101"));

        verify(slotService, times(1)).getSlotById(1L);
    }

    @Test
    void getAllSlots_ReturnsListOfSlots() throws Exception {
        List<SlotResponseDTO> slots = Arrays.asList(slotResponseDTO);
        when(slotService.getAllSlots()).thenReturn(slots);

        mockMvc.perform(get("/api/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].slotNumber").value("A-101"));

        verify(slotService, times(1)).getAllSlots();
    }

    @Test
    void getSlotsByFloor_ExistingFloorId_ReturnsSlots() throws Exception {
        List<SlotResponseDTO> slots = Arrays.asList(slotResponseDTO);
        when(slotService.getSlotsByFloor(1L)).thenReturn(slots);

        mockMvc.perform(get("/api/slots/floor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].floorNumber").value(1));

        verify(slotService, times(1)).getSlotsByFloor(1L);
    }

    @Test
    void deleteSlot_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(slotService).deleteSlot(1L);

        mockMvc.perform(delete("/api/slots/1"))
                .andExpect(status().isNoContent());

        verify(slotService, times(1)).deleteSlot(1L);
    }
}