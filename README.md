# Parking Lot Reservation System
A **Spring Boot 3.x + MySQL** backend service for managing parking floors, slots, reservations, and availability.
Designed with **clean architecture**, **validations**, **business rules**, and **extensibility** for future enhancements.
---

## Objective
Design and implement a **Parking Lot Reservation backend** with **REST API**, using **Java + Spring Boot**. The API enables parking lot administrators to manage floors and slots, and customers to reserve slots for specific time ranges without conflicts.
---

## Guidelines
- Showcase knowledge of **Spring Boot**, **JPA**, and **RESTful design**.
- Follow **best practices** in software design patterns.
- Implement the solution efficiently within **five days**.
- Write **unit test cases** with **90% to 100% code coverage**.
---

## Features
A smart parking facility with multiple floors, each containing multiple parking slots. Customers can reserve slots for a specific period, with the system preventing double bookings and calculating parking fees based on duration.

### API Endpoints
<custom-element data-json="%7B%22type%22%3A%22table-metadata%22%2C%22attributes%22%3A%7B%22title%22%3A%22API%20Endpoints%22%7D%7D" />

| Endpoint                | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `POST http://localhost:8080/api/floors`          | Create a parking floor                                                      |
| `POST http://localhost:8080/api/slots`           | Create parking slots for a floor                                            |
| `POST http://localhost:8080/api/reservations`         | Reserve a slot for a given time range (with availability and cost checks) |
| `GET http://localhost:8080/api/availability`     | List available slots for a given time range                                |
| `GET http://localhost:8080/api/reservations/{id}`| Fetch reservation details                                                    |
| `DELETE http://localhost:8080/api/reservations/{id}` | Cancel a reservation                                                    |

### Additional API Endpoints
<custom-element data-json="%7B%22type%22%3A%22table-metadata%22%2C%22attributes%22%3A%7B%22title%22%3A%22Additional%20API%20Endpoints%22%7D%7D" />

| Endpoint                | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `DELETE http://localhost:8080/api/reservations` | Delete all active/inactive reservations (for admin use)                  |

### Cost Calculation Example
**Example 1:** 4 wheeler for 2.5 hours
- **Duration:** 2.5 hours → Rounds up to 3 hours
- **Rate:** ₹30 per hour
- **Total:** ₹90

**Request Body:**
```json
{
  "slotId": 1,
  "vehicleNumber": "KA01AB1234",
  "startTime": "2025-09-02 14:00:00",
  "endTime": "2025-09-02 16:30:00"
}
```

### Business Rules
- Start time must be **before** end time.
- Reservation duration cannot exceed **24 hours**.
- Vehicle number must match the format: **`XX00XX0000`** (e.g., `KA05MH1234`).
- Partial hours are **rounded up** (e.g., 1.2 hours = 2 hours).

### Pricing
<custom-element data-json="%7B%22type%22%3A%22table-metadata%22%2C%22attributes%22%3A%7B%22title%22%3A%22Pricing%22%7D%7D" />

| Vehicle Type | Rate (per hour) |
|--------------|-----------------|
| 4 Wheeler    | Rs. 30          |
| 2 Wheeler    | Rs. 20          |
---

## Bonus Features
- **Optimistic locking** for concurrent booking protection.
- **Pagination & sorting** for availability listing.
- **API documentation** using **Swagger/OpenAPI**.
---

## Tech Stack
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL** (Relational Database)
- **Bean Validation** (`@Valid`)
- **JUnit & Mockito** (Testing)
- **Swagger/OpenAPI** (API Documentation)
- **Global Exception Handling** (`@ControllerAdvice`)
---

## Issues and Contributions

# Issues Created and Resolved

As part of the development process, the following issues were created and successfully resolved:

| Issues                                      | Status  | Description                                             |
|----------------------------------------------|---------|---------------------------------------------------------|
| Parking Lot Reservation Backend (Java + Spring Boot) | ✅ Completed | Implemented the core backend for the parking lot reservation system. |
| Add JUnit Tests for Parking Lot Reservation API      | ✅ Completed | Added comprehensive JUnit tests to ensure 90%+ code coverage. |
| SpringDoc Issue with /v3/api-docs                     | ✅ Completed | Resolved the issue faced while trying to use SpringDoc for API documentation. |

---
## 📂 Project Structure
```bash
parking_lot_reservation
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── testproject
│   │   │           └── parking_lot_reservation
│   │   │               ├── ParkingLotReservationApplication.java
│   │   │               ├── config
│   │   │               │   └── SwaggerConfig.java
│   │   │               ├── controller
│   │   │               │   ├── FloorController.java
│   │   │               │   ├── ParkingController.java
│   │   │               │   ├── ReservationController.java
│   │   │               │   └── SlotController.java
│   │   │               ├── dto
│   │   │               │   ├── AvailabilityRequestDTO.java
│   │   │               │   ├── AvailabilityResponseDTO.java
│   │   │               │   ├── ErrorResponseDTO.java
│   │   │               │   ├── FloorRequestDTO.java
│   │   │               │   ├── FloorResponseDTO.java
│   │   │               │   ├── ReservationRequestDTO.java
│   │   │               │   ├── ReservationResponseDTO.java
│   │   │               │   ├── SlotRequestDTO.java
│   │   │               │   └── SlotResponseDTO.java
│   │   │               ├── entity
│   │   │               │   ├── BaseEntity.java
│   │   │               │   ├── Floor.java
│   │   │               │   ├── Reservation.java
│   │   │               │   ├── Slot.java
│   │   │               │   └── VehicleType.java
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ParkingException.java
│   │   │               │   └── ResourceNotFoundException.java
│   │   │               ├── repository
│   │   │               │   ├── FloorRepository.java
│   │   │               │   ├── ReservationRepository.java
│   │   │               │   ├── SlotRepository.java
│   │   │               │   └── VehicleTypeRepository.java
│   │   │               ├── service
│   │   │               │   ├── FloorService.java
│   │   │               │   ├── ParkingService.java
│   │   │               │   ├── ReservationService.java
│   │   │               │   ├── SlotService.java
│   │   │               │   └── impl
│   │   │               │       ├── FloorServiceImpl.java
│   │   │               │       ├── ParkingServiceImpl.java
│   │   │               │       ├── ReservationServiceImpl.java
│   │   │               │       └── SlotServiceImpl.java
│   │   │               └── util
│   │   │                   └── VehicleNumberValidator.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── testproject
│                   └── parking_lot_reservation
│                       ├── controller
│                       │   ├── FloorControllerTest.java
│                       │   ├── ReservationControllerTest.java
│                       │   └── SlotControllerTest.java
│                       ├── service
│                       │   └── impl
│                       │       ├── FloorServiceImplTest.java
│                       │       ├── ReservationServiceImplTest.java
│                       │       └── SlotServiceImpl.java
│                       └── util
│                           └── VehicleNumberValidatorTest.java
```

---

## 🚀 Setup & Run Instructions

### Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **MySQL 8+**

### Steps
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   git checkout parking-lot-reservation
   git pull origin parking-lot-reservation
   cd parking_lot_reservation
   ```

2. **Configure MySQL:**
   - Create a database named `parking_lot_db`.
   - Update `application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc\:mysql://localhost:3306/parking_lot_db
     spring.datasource.username=<your-username>
     spring.datasource.password=<your-password>
     ```

3. **Build and run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the API:**
   - Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for Swagger documentation.

---

## 🧪 Testing
- Run unit tests:
  ```bash
  mvn test
  ```
- Ensure **90%+ code coverage** (reports generated in `target/site/jacoco`).

---

## 📄 Submission Guidelines
- **Time Duration:** 5 days from assignment.
- **Source Code:** Share your **GitHub repository link**.
- **Documentation:** Include this `README.md` with setup and run instructions.
