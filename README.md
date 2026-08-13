# Student Management System

A Spring Boot based Student Management System developed as an interview assignment.

The application provides separate Admin and Student functionality with JWT-based authentication, role-based authorization, AOP-based student validation, H2 database persistence, Swagger/OpenAPI documentation, DTO-based API communication and global exception handling.

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- Spring AOP
- H2 Database
- Swagger / OpenAPI
- Maven
- Lombok

---

## Project Structure

src/main/java
|
└── com.anshu.student_management_system
    |
    ├── Annotations
    |   └── ValidateStudent
    |
    ├── Aspect
    |   └── StudentValidationAspect
    |
    ├── Controller
    |   ├── AuthController
    |   ├── AdminController
    |   └── StudentController
    |
    ├── DTO
    |   ├── Request DTOs
    |   └── Response DTOs
    |
    ├── Entities
    |   ├── UserEntity
    |   ├── Student
    |   ├── Address
    |   └── Course
    |
    ├── ExceptionHandler
    |   ├── CustomValidationException
    |   ├── GlobalExceptionHandler
    |   └── ErrorCode
    |
    ├── Repositories
    |   ├── UserEntityRepository
    |   ├── StudentRepository
    |   ├── AddressRepository
    |   └── CourseRepository
    |
    ├── Service
    |   ├── AuthService
    |   ├── AdminService
    |   ├── StudentService
    |   └── JwtService
    |
    └── ServiceImpl
        ├── AuthServiceImpl
        ├── AdminServiceImpl
        ├── StudentServiceImpl
        └── JwtServiceImpl

---

## Application Architecture

The application follows a layered architecture:

Client
   |
   v
Controller
   |
   v
Service Interface
   |
   v
Service Implementation
   |
   v
Repository
   |
   v
Entity
   |
   v
H2 Database

DTOs are used for communication between the API and service layers so that database entities are not directly exposed through the APIs.

---

## Authentication Flow

Authentication is handled through the AuthController.

                    AuthController
                         |
          +--------------+--------------+
          |                             |
          v                             v
     Admin Login                  Student Login
          |                             |
 Username + Password            Student Code + DOB
          |                             |
          v                             v
   Validate Admin              Validate Student
          |                             |
          +--------------+--------------+
                         |
                         v
                    Generate JWT
                         |
                         v
                    Access Token

### Admin Authentication

Username + Password
        |
        v
UserEntity
        |
        v
Spring Security
        |
        v
JWT Generation
        |
        v
Admin Access Token

### Student Authentication

Student Code + Date of Birth
            |
            v
     Student Repository
            |
            v
      Validate Student
            |
            v
       Generate JWT
            |
            v
     Student Access Token

The existing Student entity is used for student authentication. A separate student user table is not required.

---

## Authorization Flow

Spring Security is used for role/authority based authorization.

Request
   |
   v
JWT Filter
   |
   v
Validate JWT
   |
   v
Create Authentication
   |
   v
Check Authority
   |
   +-------------------+
   |                   |
   v                   v
 ADMIN              STUDENT
   |                   |
   v                   v
Admin APIs         Student APIs

Admin APIs are protected using the ADMIN authority.

Student APIs are protected using the STUDENT authority.

Authorization is implemented using Spring Security annotations such as:

@PreAuthorize("hasAuthority('ADMIN')")

and:

@PreAuthorize("hasAuthority('STUDENT')")

---

## Student Validation Using AOP

Student APIs use the custom @ValidateStudent annotation.

The annotation is applied at class level so that the validation can be applied to the required Student Controller methods.

Student Request
      |
      v
JWT Authentication
      |
      v
Check STUDENT Authority
      |
      v
@ValidateStudent
      |
      v
Student Code + DOB Validation
      |
      v
Student Controller
      |
      v
Student Service
      |
      v
Repository

The AOP validation ensures that the student information supplied with the request corresponds to the student being validated.

---

## Admin Flow

The Admin manages students and courses.

Admin Login
    |
    v
JWT
    |
    v
Admin APIs
    |
    +----> Admit Student
    |
    +----> Create Course
    |
    +----> Assign Course
    |
    +----> Search Students
    |
    +----> Get Students By Course

Admin functionality includes:

- Admin registration
- Admin login
- JWT refresh
- Student admission
- Course creation
- Course assignment
- Student search
- Getting students by course

---

## Student Flow

Student Login
     |
     v
JWT
     |
     v
Student APIs
     |
     +----> Update Profile
     |
     +----> Update Address
     |
     +----> Search Courses
     |
     +----> Leave Course

Student functionality includes:

- Student login
- Profile update
- Address update
- Course search
- Leaving an enrolled course

---

## Student Address Flow

A student can have multiple addresses.

Student
   |
   +---- Permanent Address
   |
   +---- Correspondence Address
   |
   +---- Current Address

Supported address types are:

PERMANENT
CORRESPONDENCE
CURRENT

Address information is maintained using the Address entity and is associated with the student.

During profile update, an existing address can be updated using its address ID.

If an address ID supplied in the request is not found for that student, the address is skipped and the remaining valid addresses are processed.

Only successfully updated addresses are returned in the response.

---

## Course Flow

Admin
  |
  v
Create Course
  |
  v
Course Table
  |
  v
Assign Course
  |
  v
Student <----> Course

Duplicate course names are checked before creation.

For multiple course creation:

Requested Courses
       |
       +---- Existing Course --> Skip
       |
       +---- New Course ------> Create
       |
       v
Final Result

---

## Database

The application uses H2 Database, so no external database installation is required.

The main entities are:

- UserEntity
- Student
- Address
- Course
- Student-Course Relationship

H2 keeps the project self-contained and easy to run on another system.

---

## H2 Console

The H2 console can be accessed at:

http://localhost:8080/h2-console

The database connection configuration is maintained in:

src/main/resources/application.properties

Example configuration:

spring.datasource.url=jdbc:h2:file:./data/studentdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

Use the exact database URL, username and password configured in your application.properties.

---

## Viewing Database Tables

After logging into the H2 console, the following queries can be used to view the data.

### View all tables

SHOW TABLES;

### View users

SELECT * FROM USER_ENTITY;

### View students

SELECT * FROM STUDENT;

### View addresses

SELECT * FROM ADDRESS;

### View courses

SELECT * FROM COURSE;

### View student-course relationships

SELECT * FROM STUDENT_COURSES;

The exact table names may vary depending on the JPA/Hibernate naming configuration.

---

## Initial Data

Initial application data can be loaded using SQL initialization.

The SQL file can be placed inside:

src/main/resources/data.sql

For example, initial Admin and Course data can be inserted during application startup.

This allows the application to start with predefined data without manually creating everything through the APIs.

---

## Running the Application

### Prerequisites

Install:

- Java 21
- Maven

Verify Java:

java -version

Verify Maven:

mvn -version

### Build

From the project root directory:

mvn clean install

### Start

Using Maven:

mvn spring-boot:run

Or run the following class from IntelliJ IDEA:

StudentManagementSystemApplication

The application runs by default on:

http://localhost:8080

---

## Swagger / OpenAPI

Swagger is integrated into the application for API documentation and testing.

### Swagger UI

http://localhost:8080/swagger-ui/index.html

### OpenAPI Specification

http://localhost:8080/v3/api-docs

Swagger can be used to view and test the APIs.

For protected APIs, the appropriate JWT token must be provided.

Detailed API request and response examples are maintained separately in the Postman collection and Swagger documentation.

---

## Recommended Testing Flow

The recommended order for testing the application is:

1. Start Application
        |
        v
2. Verify H2 Database
        |
        v
3. Create / Login Admin
        |
        v
4. Obtain Admin JWT
        |
        v
5. Call Admin APIs
        |
        +----> Admit Student
        |
        +----> Create Course
        |
        +----> Assign Course
        |
        v
6. Login as Student
        |
        v
7. Obtain Student JWT
        |
        v
8. Call Student APIs
        |
        +----> Update Profile
        |
        +----> Update Address
        |
        +----> Search Courses
        |
        +----> Leave Course

Detailed API request and response examples are maintained separately in the Postman collection and Swagger documentation.

---

## Security Flow

Request
   |
   v
JWT Filter
   |
   v
Validate JWT
   |
   v
Create Authentication
   |
   v
Check Authority
   |
   +-------------+
   |             |
 ADMIN         STUDENT
   |             |
   v             v
Admin APIs    Student APIs
                  |
                  v
           @ValidateStudent
                  |
                  v
          Student Validation
                  |
                  v
               Service
                  |
                  v
              Repository

The application uses stateless authentication:

SessionCreationPolicy.STATELESS

Authentication is maintained through JWT rather than server-side HTTP sessions.

---

## Exception Handling

Application-specific validation errors are handled using:

CustomValidationException

A global exception handler provides a consistent error response structure.

Spring ProblemDetail is used for API error responses.

Example:

{
    "status": 403,
    "title": "Forbidden",
    "detail": "Access Denied"
}

Centralized exception handling keeps error handling separate from the controller and service logic.

---

## Design Approach

The application follows a layered Spring Boot architecture:

Controller
    |
    v
Service Interface
    |
    v
Service Implementation
    |
    v
Repository
    |
    v
Entity
    |
    v
H2 Database

Cross-cutting concerns are handled separately:

Security
   |
   +----> Spring Security
   +----> JWT
   +----> Role-based Authorization

Validation
   |
   +----> Custom Annotation
   +----> Spring AOP

API Documentation
   |
   +----> Swagger / OpenAPI

Exception Handling
   |
   +----> Global Exception Handler

Persistence
   |
   +----> Spring Data JPA
   +----> H2 Database

---

## Running the Project on Another System

Clone / Copy Project
        |
        v
Install Java 21
        |
        v
Install Maven
        |
        v
Open Project in IntelliJ IDEA
        |
        v
Verify application.properties
        |
        v
Run mvn clean install
        |
        v
Start Application
        |
        v
Open Swagger
        |
        v
Authenticate Admin / Student
        |
        v
Test APIs

No external database installation is required because the application uses H2.

---

## Configuration to Verify

Before running the application, verify the following in:

src/main/resources/application.properties

- Server port
- H2 datasource URL
- H2 username
- H2 password
- JPA/Hibernate configuration
- JWT secret key
- JWT expiration time
- SQL initialization settings

---

## Summary

The Student Management System provides:

- Admin registration and authentication
- Student authentication
- JWT-based authentication
- Role-based authorization
- Method-level authorization using @PreAuthorize
- AOP-based student validation
- Student admission
- Student profile management
- Student address management
- Course creation
- Course assignment
- Course search
- Student search
- Student-course management
- H2 database persistence
- Swagger/OpenAPI documentation
- DTO-based API communication
- Global exception handling
- Service-layer unit testing

The application follows a clean layered Spring Boot architecture with security, validation, business logic, persistence and documentation maintained as separate concerns.
