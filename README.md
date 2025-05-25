# Cab Booking System (GUI BASED)

This is a Java-based desktop application for a booking and payment system built using **Java Swing** for the GUI and **JDBC** for database connectivity. The application allows users to register, log in, book services, and make payments through a simple and interactive graphical interface.

## Features

- **User Authentication**
  - Registration and Login screens
- **Booking Interface**
  - Book services/events through a dedicated interface
- **Payment Module**
  - Handle payment input and confirmation
- **Main Menu Navigation**
  - Easy navigation between functionalities
- **Database Integration**
  - Uses JDBC for backend database connectivity

## Project Structure

| File Name            | Description |
|----------------------|-------------|
| `LoginFrame.java`     | Login screen for user authentication |
| `RegisterFrame.java`  | Registration screen for new users |
| `MainMenuFrame.java`  | Main menu with navigation options |
| `BookingFrame.java`   | Allows users to book available services |
| `PaymentFrame.java`   | Handles payment-related tasks |
| `DatabaseConnection.java` | Utility class for managing DB connections via JDBC |

## Technologies Used

- Java SE
- Swing (GUI)
- JDBC (Database Connectivity)
- MySQL (or any JDBC-compatible RDBMS)

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/booking-system.git
   cd booking-system
