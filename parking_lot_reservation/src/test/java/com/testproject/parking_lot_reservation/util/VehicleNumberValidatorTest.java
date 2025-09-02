package com.testproject.parking_lot_reservation.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VehicleNumberValidatorTest {

    @Test
    void isValid_ValidVehicleNumber_ReturnsTrue() {
        assertTrue(VehicleNumberValidator.isValid("KA01AB1234"));
        assertTrue(VehicleNumberValidator.isValid("MH02CD5678"));
        assertTrue(VehicleNumberValidator.isValid("DL03EF9012"));
    }

    @Test
    void isValid_ValidVehicleNumberWithSingleCharacter_ReturnsTrue() {
        assertTrue(VehicleNumberValidator.isValid("KA01A1234"));
        assertTrue(VehicleNumberValidator.isValid("MH02B5678"));
        assertTrue(VehicleNumberValidator.isValid("DL03C9012"));
    }

    @Test
    void isValid_NullVehicleNumber_ReturnsFalse() {
        assertFalse(VehicleNumberValidator.isValid(null));
    }

    @Test
    void isValid_EmptyVehicleNumber_ReturnsFalse() {
        assertFalse(VehicleNumberValidator.isValid(""));
        assertFalse(VehicleNumberValidator.isValid("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "INVALID",             
        "KA01AB12345",     
        "KAA1AB1234",           
        "KA01AB123",          
        "KA01AB123A",       
        "1234AB5678",       
        "KA01AB12 34",    
        "KA01-AB1234",     
        "ka01ab1234",         
        "KA01AB123",           
        "K1AB1234",             
        "KA0AB1234",            
        "KAA1B1234",            
        "KA01A1B234",         
        "KA01@B1234",           
        "KA01AB#234",          
        "KA01AB12$4",           
        "KA 01AB1234",         
        "KA01 AB1234",         
        "KA01AB 1234"           
    })
    void isValid_InvalidVehicleNumber_ReturnsFalse(String invalidNumber) {
        assertFalse(VehicleNumberValidator.isValid(invalidNumber));
    }

    @Test
    void isValid_BoundaryCases_ReturnsExpectedResults() {
        assertTrue(VehicleNumberValidator.isValid("KA01A1234"));  
        assertTrue(VehicleNumberValidator.isValid("KA01AB1234"));
        assertFalse(VehicleNumberValidator.isValid("KA01A123"));   
        assertFalse(VehicleNumberValidator.isValid("KA01AB12345")); 
    }
}