package com.testproject.parking_lot_reservation.repository;

import com.testproject.parking_lot_reservation.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {
}