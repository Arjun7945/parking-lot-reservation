# 🚗 Parking Lot Reservation System

A Spring Boot 3.x + MySQL backend service for managing parking floors, slots, reservations, and availability.  
Designed with clean architecture, validations, business rules, and extensibility for future enhancements.

---

## 📌 Features

- Create and manage **floors** and **slots**  
- **Reserve slots** with validation and conflict checks  
- **Hourly pricing** by vehicle type (configurable)  
- Fetch **reservation details**  
- **Cancel reservations**  
- List **available slots** for a given time range  
- **Business rules enforced**:
  - Start time < End time  
  - Reservation ≤ 24 hours  
  - Vehicle number format `XX00XX0000` (e.g., KA05MH1234)  
  - Partial hours charged as full hours  

### 🔥 Bonus Features
- Optimistic locking for **concurrent booking protection**  
- Pagination & sorting for availability listing  
- API documentation with **Swagger / OpenAPI**  

---

## 🛠 Tech Stack

- **Java 17+**  
- **Spring Boot 3.x**  
- **Spring Data JPA**  
- **MySQL**  
- **Bean Validation**  
- **JUnit & Mockito** (testing)  
- **Swagger/OpenAPI** (API documentation)  

---

## 📂 Project Structure

