# Final Project Report - Event Photography Booking System

## Overview
This report outlines the successful integration of all sub-systems into the final Event Photography and Videography Booking System. The system was broken down into modular components, managed independently by team members, and finally integrated into a cohesive, centralized Spring Boot application by Member 6 (Admin & Integration).

## Module Integration (Member 6)
- **User Management (Member 1)**: Integrated `RegularUser` and `PremiumUser` functionalities. Extended base `User` to include the `Admin` model.
- **Photographer Management (Member 2)**: Linked the photographer repository with the core system.
- **Booking Management (Member 3)**: Unified the booking validation logic to cross-reference existing Packages and Users.
- **Package Management (Member 4)**: Centralized the package catalogue which is now heavily utilized by the frontend SPA.
- **Payment Management (Member 5)**: Embedded the payment ledger system supporting polymorphic `CardPayment` and `CashPayment`.

## Admin Capabilities
An exclusive `AdminController` was established to serve the unified Admin Dashboard, summarizing realtime statistics including active users, total photographers on roster, booking metrics, and revenue generated.

## System Architecture
The application runs on a Spring Boot 3 backend utilizing RESTful APIs to communicate with a Single Page Application (SPA) frontend. Persistence is currently managed via structured File I/O (`.txt` ledger files for each entity).

*For a complete view of the Object-Oriented design applied, please refer to `UML_Diagram.md`.*
