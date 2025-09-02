package com.testproject.parking_lot_reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.dto.SlotResponseDTO;
import com.testproject.parking_lot_reservation.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Slot Management", description = "APIs for managing parking slots")
public class SlotController {

    private final SlotService slotService;

    @PostMapping
    @Operation(summary = "Create a new parking slot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slot created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Floor or vehicle type not found"),
        @ApiResponse(responseCode = "409", description = "Slot number already exists on this floor")
    })
    public ResponseEntity<SlotResponseDTO> createSlot(@Valid @RequestBody SlotRequestDTO slotRequestDTO) {
        SlotResponseDTO response = slotService.createSlot(slotRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get slot by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slot found"),
        @ApiResponse(responseCode = "404", description = "Slot not found")
    })
    public ResponseEntity<SlotResponseDTO> getSlot(@PathVariable Long id) {
        SlotResponseDTO response = slotService.getSlotById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all slots")
    @ApiResponse(responseCode = "200", description = "List of all slots")
    public ResponseEntity<List<SlotResponseDTO>> getAllSlots() {
        List<SlotResponseDTO> responses = slotService.getAllSlots();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/floor/{floorId}")
    @Operation(summary = "Get slots by floor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slots found"),
        @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByFloor(@PathVariable Long floorId) {
        List<SlotResponseDTO> responses = slotService.getSlotsByFloor(floorId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/vehicle-type/{vehicleTypeId}")
    @Operation(summary = "Get slots by vehicle type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slots found"),
        @ApiResponse(responseCode = "404", description = "Vehicle type not found")
    })
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByVehicleType(@PathVariable Long vehicleTypeId) {
        List<SlotResponseDTO> responses = slotService.getSlotsByVehicleType(vehicleTypeId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a slot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Slot deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Slot not found")
    })
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
