package com.testproject.parking_lot_reservation.exception;

public class ParkingException extends RuntimeException {
    public ParkingException(String message) {
        super(message);
    }
}