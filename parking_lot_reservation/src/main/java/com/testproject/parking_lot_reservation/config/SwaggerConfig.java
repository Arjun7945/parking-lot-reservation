package com.testproject.parking_lot_reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parking Lot Reservation API")
                        .version("1.0.0")
                        .description("API for managing parking lot reservations, floors, slots, and reservations")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@parkinglot.com")));
    }
}