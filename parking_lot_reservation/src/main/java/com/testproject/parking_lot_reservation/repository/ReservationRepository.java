package com.testproject.parking_lot_reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.testproject.parking_lot_reservation.entity.Reservation;
import com.testproject.parking_lot_reservation.entity.Slot;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND r.isActive = true " +
           "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Reservation> findConflictingReservations(@Param("slotId") Long slotId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT r FROM Reservation r WHERE r.vehicleNumber = :vehicleNumber AND r.isActive = true " +
           "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Reservation> findVehicleReservations(@Param("vehicleNumber") String vehicleNumber,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    // Change this to find both active and inactive reservations
    Optional<Reservation> findByIdAndIsActiveTrue(Long id);
    
    // Add this method to find reservation by ID regardless of active status
    Optional<Reservation> findById(Long id);
    
    @Query("SELECT s FROM Slot s WHERE s.id NOT IN (" +
           "SELECT r.slot.id FROM Reservation r WHERE r.isActive = true " +
           "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))" +
           ") AND s.isAvailable = true " +
           "AND (:vehicleTypeId IS NULL OR s.vehicleType.id = :vehicleTypeId)")
    List<Slot> findAvailableSlots(@Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime,
                                 @Param("vehicleTypeId") Long vehicleTypeId);

    List<Reservation> findAll();

    List<Reservation> findByIsActiveTrue();
}