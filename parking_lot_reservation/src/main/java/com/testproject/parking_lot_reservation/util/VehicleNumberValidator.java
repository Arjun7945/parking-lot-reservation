package com.testproject.parking_lot_reservation.util;

public class VehicleNumberValidator {
    private static final String VEHICLE_NUMBER_REGEX = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
    
    public static boolean isValid(String vehicleNumber) {
        return vehicleNumber != null && vehicleNumber.matches(VEHICLE_NUMBER_REGEX);
    }
}