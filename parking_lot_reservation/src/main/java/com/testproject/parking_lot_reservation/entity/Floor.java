package com.testproject.parking_lot_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "floors")
@Getter
@Setter
public class Floor extends BaseEntity {
    @Column(unique = true, nullable = false)
    private Integer floorNumber;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer totalSlots;
}