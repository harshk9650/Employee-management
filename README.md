# 🏢 Employee Management System

A production-grade **RESTful backend** built with **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **Hibernate**, and **MySQL** — implementing complete CRUD operations for employee records.

---

## 📁 Project Structure

```
employee-management/
├── src/
│   └── main/
│       ├── java/com/ems/
│       │   ├── EmployeeManagementApplication.java   ← App entry point
│       │   ├── controller/
│       │   │   └── EmployeeController.java          ← REST endpoints
│       │   ├── service/
│       │   │   └── EmployeeService.java             ← Service interface
│       │   ├── serviceimpl/
│       │   │   └── EmployeeServiceImpl.java         ← Business logic
│       │   ├── repository/
│       │   │   └── EmployeeRepository.java          ← JPA repository
│       │   ├── entity/
│       │   │   └── Employee.java                   ← DB entity (JPA)
│       │   ├── dto/
│       │   │   ├── EmployeeDTO.java                 ← Request/response DTO
│       │   │   └── ApiResponse.java                ← Generic API wrapper
│       │   └── exception/
│       │       ├── ResourceNotFoundException.java  ← 404 exception
│       │       ├── DuplicateEmailException.java    ← 409 exception
│       │       └── GlobalExceptionHandler.java     ← Centralised error handling
│       └── resources/
│           └── application.properties              ← DB & JPA config
├── schema.sql                                      ← MySQL setup + sample data
├── EmployeeManagement.postman_collection.json      ← Postman tests
└── pom.xml                                         ← Maven dependencies
```

---

## 🛠️ Technology Stack

| Layer            | Technology                        |
|-----------------|-----------------------------------|
| Language         | Java 17                           |
| Framework        | Spring Boot 3.2.5                 |
| Persistence      | Spring Data JPA + Hibernate       |
| Database         | MySQL 8                           |
| Build Tool       | Maven                             |
| Validation       | Jakarta Bean Validation           |
| Boilerplate      | Lombok                            |
| Testing          | Postman                           |

---

## 🗃️ Database Schema

```sql
CREATE TABLE employee (
    id          BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    email       VARCHAR(150)   NOT NULL UNIQUE,
    department  VARCHAR(100)   NOT NULL,
    designation VARCHAR(100)   NOT NULL,
    salary      DECIMAL(15,2)  NOT NULL CHECK (salary > 0)
);
```

---

## 🚀 REST API Endpoints

| Method   | Endpoint            | Description              | Status Code        |
|----------|---------------------|--------------------------|-------------------|
| `POST`   | `/employees`        | Create new employee      | `201 Created`     |
| `GET`    | `/employees`        | Get all employees        | `200 OK`          |
| `GET`    | `/employees/{id}`   | Get employee by ID       | `200 OK`          |
| `PUT`    | `/employees/{id}`   | Update employee          | `200 OK`          |
| `DELETE` | `/employees/{id}`   | Delete employee          | `200 OK`          |

---

## 📋 Sample API Requests

### ✅ Create Employee — `POST /employees`
```json
// Request Body
{
  "name": "Arjun Sharma",
  "email": "arjun@company.com",
  "department": "Engineering",
  "designation": "Senior Software Engineer",
  "salary": 95000.00
}

// Response — 201 Created
{
  "success": true,
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "name": "Arjun Sharma",
    "email": "arjun@company.com",
    "department": "Engineering",
    "designation": "Senior Software Engineer",
    "salary": 95000.0
  },
  "timestamp": "2024-06-10T10:30:00"
}
```

### ✅ Get All Employees — `GET /employees`
```json
// Response — 200 OK
{
  "success": true,
  "message": "Fetched 5 employee(s) successfully",
  "data": [ { "id": 1, ... }, { "id": 2, ... } ],
  "timestamp": "2024-06-10T10:31:00"
}
```

### ❌ Not Found — `GET /employees/999`
```json
// Response — 404 Not Found
{
  "success": false,
  "message": "Employee not found with id : '999'",
  "timestamp": "2024-06-10T10:32:00"
}
```

### ❌ Validation Error — `POST /employees` with invalid data
```json
// Response — 400 Bad Request
{
  "success": false,
  "message": "Validation failed. Please check the errors below.",
  "data": {
    "name": "Employee name is required",
    "email": "Email must be a valid format",
    "salary": "Salary must be greater than 0"
  }
}
```

---

## ⚙️ Setup & Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

### 1. Clone the repository
```bash
git clone https://github.com/your-username/employee-management.git
cd employee-management
```

### 2. Set up the database
```bash
mysql -u root -p < schema.sql
```

### 3. Configure `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The API will be live at `http://localhost:8080/employees` ✅

---

## 🧪 Testing with Postman

1. Open Postman
2. Click **Import** → select `EmployeeManagement.postman_collection.json`
3. Run requests in order (1 → 10) to test all scenarios

---

## 🏗️ Architecture

```
Client (Postman / Frontend)
         │  HTTP Request
         ▼
┌─────────────────────┐
│   EmployeeController│  ← @RestController: handles HTTP, delegates to service
└────────┬────────────┘
         │ calls
         ▼
┌─────────────────────┐
│ EmployeeServiceImpl │  ← @Service: business logic, validation, mapping
└────────┬────────────┘
         │ calls
         ▼
┌─────────────────────┐
│ EmployeeRepository  │  ← @Repository: JPA/Hibernate, talks to MySQL
└────────┬────────────┘
         │ SQL
         ▼
┌─────────────────────┐
│      MySQL DB       │  ← employee_management database
└─────────────────────┘
```

**Exception flow** — any layer can throw; `GlobalExceptionHandler` catches all and returns structured JSON.

---

## 📄 License

MIT License — free to use for learning and projects.
