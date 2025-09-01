package com.testproject.parking_lot_reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.testproject.parking_lot_reservation.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query("SELECT s FROM Slot s WHERE NOT EXISTS (" +
           "SELECT r FROM Reservation r WHERE r.slot.id = s.id AND " +
           "r.startTime < :endTime AND r.endTime > :startTime)")
    List<Slot> findAvailableSlots(@Param("startTime") LocalDateTime startTime, 
                                 @Param("endTime") LocalDateTime endTime);
}