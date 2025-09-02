package com.testproject.parking_lot_reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.testproject.parking_lot_reservation.entity.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    Optional<Slot> findBySlotNumberAndFloorId(String slotNumber, Long floorId);
    List<Slot> findByFloorId(Long floorId);
    List<Slot> findByVehicleTypeId(Long vehicleTypeId);
    
    @Query("SELECT s FROM Slot s WHERE s.vehicleType.id = :vehicleTypeId AND s.isAvailable = true")
    List<Slot> findAvailableSlotsByVehicleType(@Param("vehicleTypeId") Long vehicleTypeId);
}