package com.testproject.parking_lot_reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle_types")
@Getter
@Setter
public class VehicleType extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String type;
    
    @Column(nullable = false)
    private Double hourlyRate;
    
    @Column(nullable = false)
    private String description;
}