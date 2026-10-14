CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN'
);

CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    duration_years INT,
    total_semesters INT,
    fees DECIMAL(10,2)
);

CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    gender VARCHAR(10),
    dob DATE,
    address VARCHAR(255),
    course_id INT,
    admission_date DATE DEFAULT (CURRENT_DATE),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

CREATE TABLE marks (
    mark_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    marks_obtained DECIMAL(5,2),
    max_marks DECIMAL(5,2) DEFAULT 100,
    exam_type VARCHAR(30),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject VARCHAR(100),
    attendance_date DATE NOT NULL,
    status VARCHAR(10),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT INTO courses (course_name, duration_years, total_semesters, fees)
VALUES ('B.Tech CSE', 4, 8, 400000.00);