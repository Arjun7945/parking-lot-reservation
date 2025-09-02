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
        "INVALID",              // Too short
        "KA01AB12345",          // Too long (11 characters)
        "KAA1AB1234",           // Extra character in first part (should be 2 letters, 2 numbers)
        "KA01AB123",            // Missing digit in last part (should be 4 digits)
        "KA01AB123A",           // Letter in number part
        "1234AB5678",           // Numbers instead of letters at start
        "KA01AB12 34",          // Contains space
        "KA01-AB1234",          // Contains hyphen
        "ka01ab1234",           // Lowercase letters
        "KA01AB123",            // Only 3 digits at end
        "K1AB1234",             // Missing digits (should be 2 letters, 2 numbers)
        "KA0AB1234",            // Missing digit (should be 2 numbers)
        "KAA1B1234",            // Wrong format
        "KA01A1B234",           // Wrong format
        "KA01@B1234",           // Special character
        "KA01AB#234",           // Special character
        "KA01AB12$4",           // Special character
        "KA 01AB1234",          // Contains space
        "KA01 AB1234",          // Contains space
        "KA01AB 1234"           // Contains space
    })
    void isValid_InvalidVehicleNumber_ReturnsFalse(String invalidNumber) {
        assertFalse(VehicleNumberValidator.isValid(invalidNumber));
    }

    @Test
    void isValid_BoundaryCases_ReturnsExpectedResults() {
        assertTrue(VehicleNumberValidator.isValid("KA01A1234"));    // 9 characters - valid
        assertTrue(VehicleNumberValidator.isValid("KA01AB1234"));   // 10 characters - valid
        assertFalse(VehicleNumberValidator.isValid("KA01A123"));    // 8 characters - invalid
        assertFalse(VehicleNumberValidator.isValid("KA01AB12345")); // 11 characters - invalid
    }
}