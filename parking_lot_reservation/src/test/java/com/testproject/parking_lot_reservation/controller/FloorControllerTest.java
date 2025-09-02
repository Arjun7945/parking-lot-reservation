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
import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.FloorResponseDTO;
import com.testproject.parking_lot_reservation.service.FloorService;

@WebMvcTest(FloorController.class)
class FloorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FloorService floorService;

    private FloorRequestDTO floorRequestDTO;
    private FloorResponseDTO floorResponseDTO;

    @BeforeEach
    void setUp() {
        floorRequestDTO = new FloorRequestDTO();
        floorRequestDTO.setFloorNumber(1);
        floorRequestDTO.setName("Ground Floor");
        floorRequestDTO.setTotalSlots(50);

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
    void createFloor_ValidRequest_ReturnsCreatedFloor() throws Exception {
        when(floorService.createFloor(any(FloorRequestDTO.class))).thenReturn(floorResponseDTO);

        mockMvc.perform(post("/api/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(floorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.floorNumber").value(1))
                .andExpect(jsonPath("$.name").value("Ground Floor"));

        verify(floorService, times(1)).createFloor(any(FloorRequestDTO.class));
    }

    @Test
    void createFloor_InvalidRequest_ReturnsBadRequest() throws Exception {
        floorRequestDTO.setFloorNumber(null);

        mockMvc.perform(post("/api/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(floorRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(floorService, never()).createFloor(any(FloorRequestDTO.class));
    }

    @Test
    void getFloor_ExistingId_ReturnsFloor() throws Exception {
        when(floorService.getFloorById(1L)).thenReturn(floorResponseDTO);

        mockMvc.perform(get("/api/floors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.floorNumber").value(1));

        verify(floorService, times(1)).getFloorById(1L);
    }

    @Test
    void getAllFloors_ReturnsListOfFloors() throws Exception {
        List<FloorResponseDTO> floors = Arrays.asList(floorResponseDTO);
        when(floorService.getAllFloors()).thenReturn(floors);

        mockMvc.perform(get("/api/floors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].floorNumber").value(1));

        verify(floorService, times(1)).getAllFloors();
    }

    @Test
    void deleteFloor_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(floorService).deleteFloor(1L);

        mockMvc.perform(delete("/api/floors/1"))
                .andExpect(status().isNoContent());

        verify(floorService, times(1)).deleteFloor(1L);
    }
}