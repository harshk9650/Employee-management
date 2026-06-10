-- ============================================================
-- Employee Management System - MySQL Schema
-- ============================================================
-- Run this script in MySQL Workbench, DBeaver, or MySQL CLI
-- before starting the Spring Boot application.
--
-- How to run:
--   mysql -u root -p < schema.sql
-- ============================================================

-- Step 1: Create the database (if it doesn't already exist)
CREATE DATABASE IF NOT EXISTS employee_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Step 2: Switch to the database
USE employee_management;

-- Step 3: Drop existing table (useful for fresh start during development)
-- WARNING: This deletes ALL employee data. Remove in production.
DROP TABLE IF EXISTS employee;

-- Step 4: Create the employee table
CREATE TABLE employee (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL,
    department  VARCHAR(100)    NOT NULL,
    designation VARCHAR(100)    NOT NULL,
    salary      DECIMAL(15, 2)  NOT NULL,

    -- Primary Key
    CONSTRAINT pk_employee PRIMARY KEY (id),

    -- Unique constraint on email (no two employees can share an email)
    CONSTRAINT uq_employee_email UNIQUE (email),

    -- Check constraints (MySQL 8.0.16+)
    CONSTRAINT chk_salary_positive CHECK (salary > 0)
);

-- Step 5: Add indexes for frequently queried columns
-- Index on department for fast filtering by department
CREATE INDEX idx_employee_department ON employee(department);

-- Index on email for fast lookup during uniqueness checks
CREATE INDEX idx_employee_email ON employee(email);

-- ============================================================
-- Step 6: Insert sample data for testing
-- ============================================================

INSERT INTO employee (name, email, department, designation, salary) VALUES
('Arjun Sharma',     'arjun.sharma@company.com',   'Engineering',  'Senior Software Engineer', 95000.00),
('Priya Verma',      'priya.verma@company.com',    'Engineering',  'Backend Developer',        78000.00),
('Rahul Gupta',      'rahul.gupta@company.com',    'HR',           'HR Manager',               65000.00),
('Sneha Patel',      'sneha.patel@company.com',    'Finance',      'Financial Analyst',        72000.00),
('Amit Kumar',       'amit.kumar@company.com',     'Engineering',  'DevOps Engineer',          88000.00),
('Deepika Singh',    'deepika.singh@company.com',  'Marketing',    'Marketing Lead',           70000.00),
('Vikram Nair',      'vikram.nair@company.com',    'Engineering',  'Tech Lead',               110000.00),
('Anjali Mehta',     'anjali.mehta@company.com',   'HR',           'HR Executive',             55000.00),
('Rohan Joshi',      'rohan.joshi@company.com',    'Finance',      'Accounts Manager',         68000.00),
('Kavya Reddy',      'kavya.reddy@company.com',    'Engineering',  'Junior Developer',         52000.00);

-- ============================================================
-- Verification queries (run these to confirm everything is ok)
-- ============================================================
-- SELECT * FROM employee;
-- SELECT COUNT(*) AS total_employees FROM employee;
-- DESCRIBE employee;
-- SHOW CREATE TABLE employee;
