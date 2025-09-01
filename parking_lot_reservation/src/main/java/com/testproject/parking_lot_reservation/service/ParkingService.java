package com.testproject.parking_lot_reservation.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testproject.parking_lot_reservation.dto.FloorRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationRequestDTO;
import com.testproject.parking_lot_reservation.dto.ReservationResponseDTO;
import com.testproject.parking_lot_reservation.dto.SlotRequestDTO;
import com.testproject.parking_lot_reservation.entity.Floor;
import com.testproject.parking_lot_reservation.entity.Reservation;
import com.testproject.parking_lot_reservation.entity.Slot;
import com.testproject.parking_lot_reservation.entity.VehicleType;
import com.testproject.parking_lot_reservation.exception.ParkingException;
import com.testproject.parking_lot_reservation.repository.FloorRepository;
import com.testproject.parking_lot_reservation.repository.ReservationRepository;
import com.testproject.parking_lot_reservation.repository.SlotRepository;
import com.testproject.parking_lot_reservation.repository.VehicleTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    @Transactional
    public Floor createFloor(FloorRequestDTO request) {
        Floor floor = new Floor();
        floor.setFloorNumber(request.getFloorNumber());
        return floorRepository.save(floor);
    }

    @Transactional
    public Slot createSlot(SlotRequestDTO request) {
        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new ParkingException("Floor not found"));
        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                .orElseThrow(() -> new ParkingException("Vehicle type not found"));

        Slot slot = new Slot();
        slot.setSlotNumber(request.getSlotNumber());
        slot.setFloor(floor);
        slot.setVehicleType(vehicleType);
        return slotRepository.save(slot);
    }

    @Transactional
    public ReservationResponseDTO reserveSlot(ReservationRequestDTO request) {
        validateReservationRequest(request);

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ParkingException("Slot not found"));

        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                request.getSlotId(), request.getStartTime(), request.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new ParkingException("Slot is not available for the requested time range");
        }

        double cost = calculateCost(slot.getVehicleType().getHourlyRate(), request.getStartTime(), request.getEndTime());

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setVehicleNumber(request.getVehicleNumber());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setCost(cost);

        reservation = reservationRepository.save(reservation);

        return mapToReservationResponseDTO(reservation);
    }

    public Page<Slot> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        validateTimeRange(startTime, endTime);
        List<Slot> availableSlots = slotRepository.findAvailableSlots(startTime, endTime);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), availableSlots.size());
        List<Slot> paginatedSlots = (start < availableSlots.size()) ? availableSlots.subList(start, end) : List.of();
        return new PageImpl<>(paginatedSlots, pageable, availableSlots.size());
    }

    public ReservationResponseDTO getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ParkingException("Reservation not found"));
        return mapToReservationResponseDTO(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ParkingException("Reservation not found"));
        reservationRepository.delete(reservation);
    }

    private void validateReservationRequest(ReservationRequestDTO request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new ParkingException("Start time must be before end time");
        }
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        if (duration.toHours() > 24) {
            throw new ParkingException("Reservation duration cannot exceed 24 hours");
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new ParkingException("Start time must be before end time");
        }
    }

    private double calculateCost(double hourlyRate, LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long hours = (long) Math.ceil(duration.toMinutes() / 60.0); // Round up partial hours
        return hourlyRate * hours;
    }

    private ReservationResponseDTO mapToReservationResponseDTO(Reservation reservation) {
        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(reservation.getId());
        response.setSlotId(reservation.getSlot().getId());
        response.setVehicleNumber(reservation.getVehicleNumber());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setCost(reservation.getCost());
        return response;
    }
}