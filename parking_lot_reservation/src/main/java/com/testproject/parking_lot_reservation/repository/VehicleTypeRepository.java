package com.testproject.parking_lot_reservation.repository;

import com.testproject.parking_lot_reservation.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
}