     package com.testproject.parking_lot_reservation.entity;

     import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

     @Data
     @Entity
     @Table(name = "floors")
     public class Floor {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;

         @Column(nullable = false)
         private String floorNumber;

         @JsonIgnore
         @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
         private List<Slot> slots;

         @Version
         private Long version;
     }