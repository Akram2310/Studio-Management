# Studio Management System

A JavaFX desktop application for managing a photo/media studio, backed by a MySQL database (JDBC).

## Features
- **Sign up / login** – users are stored in MySQL; passwords are salted and hashed (PBKDF2-HMAC-SHA256)
- **Booking** – save a customer booking (name + date) to the database
- **Payment form** – card-detail input with validation (no real payment processing)
- **Equipment management** – add and delete studio equipment, loaded from the database on startup
- **Admin screen** – user-creation form UI (not yet connected to the database)

## Tech stack
Java 8+, JavaFX / FXML, MySQL, JDBC (prepared statements), NetBeans

## Setup
1. Install MySQL (or XAMPP) and run `schema.sql`. Change the `CHANGE_ME` password in it first.
2. Copy `config.example.properties` to `config.properties` and enter the same database password.
3. Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) and add its jar to the project libraries in NetBeans.
4. Run the project (main class: `StudioManagmentSystem.Login.LoginScreen`).

## Project structure
```
src/
├── StudioManagmentSystem/
│   ├── Login/  Sign/  Booking/  Payment/  Equipmanage/  UserManager/   (FXML + controllers)
│   ├── DatabaseConnector.java   (connection, reads config)
│   └── UserDAO.java             (users, equipment queries)
└── Images/
```

## Known limitations
- Payment screen validates input only; no payment is processed
- Admin screen does not save users yet
- Admin button on the login screen is not access-controlled
