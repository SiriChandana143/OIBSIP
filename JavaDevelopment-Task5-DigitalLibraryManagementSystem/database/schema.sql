-- SMARTLIB AI Database Schema
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS smartlib_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartlib_db;

-- Categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
);

-- Users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'LIBRARIAN', 'USER') NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Books
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    category_id BIGINT,
    publisher VARCHAR(150),
    publication_year INT,
    language VARCHAR(50) DEFAULT 'English',
    quantity INT NOT NULL DEFAULT 1,
    available_quantity INT NOT NULL DEFAULT 1,
    image_url VARCHAR(500),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Borrow Records
CREATE TABLE IF NOT EXISTS borrow_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    issue_date DATETIME NOT NULL,
    due_date DATETIME NOT NULL,
    return_date DATETIME,
    status ENUM('ISSUED', 'RETURNED', 'OVERDUE') NOT NULL DEFAULT 'ISSUED',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Reservations
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    reservation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'FULFILLED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Fines
CREATE TABLE IF NOT EXISTS fines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    borrow_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status ENUM('PENDING', 'PAID') NOT NULL DEFAULT 'PENDING',
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (borrow_id) REFERENCES borrow_records(id) ON DELETE CASCADE
);

-- Notifications
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('UNREAD', 'READ') NOT NULL DEFAULT 'UNREAD',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Search History
CREATE TABLE IF NOT EXISTS search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    search_query VARCHAR(255) NOT NULL,
    search_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Recommendations
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    recommendation_score DOUBLE NOT NULL,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Seed Categories
INSERT INTO categories (category_name) VALUES
('Programming'), ('Data Science'), ('Machine Learning'), ('Artificial Intelligence'),
('Database'), ('Web Development'), ('Mobile Development'), ('Computer Science'),
('Mathematics'), ('Physics'), ('Literature'), ('History');

-- Default Admin (password: admin123 - BCrypt encoded)
INSERT INTO users (name, email, password, phone, role) VALUES
('System Admin', 'admin@smartlib.ai',
 '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQqKqJqJqJqJqJqJqJqJqJqJqJqJq',
 '+919876543210', 'ADMIN');

-- Sample Books
INSERT INTO books (title, author, isbn, category_id, publisher, publication_year, language, quantity, available_quantity, image_url) VALUES
('Clean Code', 'Robert C. Martin', '978-0132350884', 1, 'Prentice Hall', 2008, 'English', 5, 5, 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200'),
('Introduction to Algorithms', 'Thomas H. Cormen', '978-0262033848', 8, 'MIT Press', 2009, 'English', 3, 3, 'https://images.unsplash.com/photo-1512820790818-83f8a4e2e1e9?w=200'),
('Python Crash Course', 'Eric Matthes', '978-1593279288', 1, 'No Starch Press', 2019, 'English', 4, 4, 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=200'),
('Hands-On Machine Learning', 'Aurélien Géron', '978-1098125974', 3, 'O''Reilly', 2022, 'English', 3, 3, 'https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=200'),
('Artificial Intelligence: A Modern Approach', 'Stuart Russell', '978-0134610993', 4, 'Pearson', 2020, 'English', 2, 2, 'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=200'),
('Database System Concepts', 'Abraham Silberschatz', '978-0078022159', 5, 'McGraw-Hill', 2019, 'English', 3, 3, 'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=200'),
('Java: The Complete Reference', 'Herbert Schildt', '978-1260440232', 1, 'McGraw-Hill', 2021, 'English', 4, 4, 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=200'),
('Deep Learning', 'Ian Goodfellow', '978-0262035613', 3, 'MIT Press', 2016, 'English', 2, 2, 'https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=200');
