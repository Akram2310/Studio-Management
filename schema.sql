CREATE DATABASE IF NOT EXISTS studio;
USE studio;

CREATE USER IF NOT EXISTS 'studio'@'localhost' IDENTIFIED BY 'A12345';
GRANT ALL PRIVILEGES ON studio.* TO 'studio'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS userPersonalInformation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
    age VARCHAR(10),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL   -- stores salt:hash (PBKDF2), never plain text
);

CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    booking_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS equipment (
    equipment_id VARCHAR(50) PRIMARY KEY,
    equipment_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL
);
