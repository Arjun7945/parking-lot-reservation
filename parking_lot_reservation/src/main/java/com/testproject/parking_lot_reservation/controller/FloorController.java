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
import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.FloorResponseDTO;
import com.testproject.parking_lot_reservation.service.FloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
@Tag(name = "Floor Management", description = "APIs for managing parking floors")
public class FloorController {

    private final FloorService floorService;

    @PostMapping
    @Operation(summary = "Create a new parking floor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Floor created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Floor number already exists")
    })
    public ResponseEntity<FloorResponseDTO> createFloor(@Valid @RequestBody FloorRequestDTO floorRequestDTO) {
        FloorResponseDTO response = floorService.createFloor(floorRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get floor by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Floor found"),
        @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    public ResponseEntity<FloorResponseDTO> getFloor(@PathVariable Long id) {
        FloorResponseDTO response = floorService.getFloorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{floorNumber}")
    @Operation(summary = "Get floor by floor number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Floor found"),
        @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    public ResponseEntity<FloorResponseDTO> getFloorByNumber(@PathVariable Integer floorNumber) {
        FloorResponseDTO response = floorService.getFloorByNumber(floorNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all floors")
    @ApiResponse(responseCode = "200", description = "List of all floors")
    public ResponseEntity<List<FloorResponseDTO>> getAllFloors() {
        List<FloorResponseDTO> responses = floorService.getAllFloors();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a floor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Floor deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        floorService.deleteFloor(id);
        return ResponseEntity.noContent().build();
    }
}
