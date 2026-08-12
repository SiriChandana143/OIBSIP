-- schema.sql
-- Online Reservation System Database Schema for SQLite

-- 1. Create 'users' table for storing login credentials
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);

-- 2. Create 'reservations' table for storing booked tickets
CREATE TABLE IF NOT EXISTS reservations (
    pnr TEXT PRIMARY KEY,
    passenger_name TEXT NOT NULL,
    train_no TEXT NOT NULL,
    train_name TEXT NOT NULL,
    class_type TEXT NOT NULL,
    journey_date TEXT NOT NULL,
    source TEXT NOT NULL,
    destination TEXT NOT NULL
);

-- 3. Insert default admin credentials
INSERT OR IGNORE INTO users (username, password) VALUES ('admin', 'admin123');
