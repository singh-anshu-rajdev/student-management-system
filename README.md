# Student Management System

A Spring Boot based Student Management System developed as part of a backend development assignment.

The application provides functionality for administrator management, student admission, course management, course assignment, student profile management and course search.

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate
- H2 Database
- Spring AOP
- Swagger / OpenAPI
- Maven
- Lombok

---

## Project Structure

    student-management-system
    │
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com.anshu.student_management_system
    │   │   │       │
    │   │   │       ├── Annotations
    │   │   │       ├── Config
    │   │   │       ├── Controller
    │   │   │       ├── DTO
    │   │   │       ├── Entities
    │   │   │       ├── ExceptionHandler
    │   │   │       ├── Repositories
    │   │   │       ├── Service
    │   │   │       │   └── ServiceImpl
    │   │   │       ├── Utilities
    │   │   │       └── StudentManagementSystemApplication
    │   │   │
    │   │   └── resources
    │   │       ├── application.properties
    │   │       └── data.sql
    │   │
    │   └── test
    │
    ├── pom.xml
    └── README.md

---

## Application Architecture

The application follows a layered architecture.

    Client
      |
      v
    Controller
      |
      v
    Service
      |
      v
    Repository
      |
      v
    JPA / Hibernate
      |
      v
    H2 Database

### Layer Responsibilities

- **Controller** - Handles HTTP requests and responses.
- **Service** - Contains business logic.
- **Repository** - Handles database operations.
- **Entity** - Represents database tables.
- **DTO** - Used for transferring data between application layers.
- **ExceptionHandler** - Provides centralized exception handling.
- **Config** - Contains application and security configuration.
- **Utilities** - Contains enums and common application utilities.
- **Annotations** - Contains custom annotations used by the application.

---

## Security Flow

Admin authentication is implemented using Spring Security and JWT.

    Login Request
         |
         v
    Admin Controller
         |
         v
    Authentication Manager
         |
         v
    User Repository
         |
         v
    Validate Username / Password
         |
         v
    JWT Service
         |
         v
    Access Token
         |
         v
    Client

For subsequent secured requests:

    Client Request
         |
         v
    JWT Filter
         |
         v
    Validate JWT
         |
         v
    Spring Security
         |
         v
    Controller
         |
         v
    Service

JWT is used to authenticate administrator requests without maintaining a server-side session.

---

## Student Validation

Student APIs use student-specific validation based on:

- Student Code
- Date of Birth

These values are provided through request headers.

A custom annotation and AOP-based validation mechanism is used so that the validation logic does not have to be repeated in every student API.

    Student Request
         |
         v
    @ValidateStudent
         |
         v
    AOP Validation
         |
         v
    Student Repository
         |
         +---- Student Valid ----> Student Controller
         |
         +---- Student Invalid --> Exception Handler

This keeps the student validation logic centralized and reusable.

---

## Main Application Flows

### Admin Flow

    Admin
      |
      +--> Register
      |
      +--> Login
      |      |
      |      +--> JWT Token
      |
      +--> Refresh Token
      |
      +--> Admit Student
      |
      +--> Create Course
      |
      +--> Assign Course
      |
      +--> Search Students
      |
      +--> Find Students by Course

### Student Flow

    Student
      |
      v
    Student Code + Date of Birth Validation
      |
      +--> Update Profile
      |
      +--> Search Courses
      |
      +--> Leave Course

---

## Student Admission Flow

Student admission creates the student and associated address information.

    Admin
      |
      v
    Admit Student
      |
      v
    Validate Student Code
      |
      v
    Create Student
      |
      +--> Student Details
      |
      +--> Email
      |
      +--> Mobile Number
      |
      +--> Parent Names
      |
      +--> Address
      |
      v
    Save to H2

A student can have multiple addresses.

    Student
       |
       +---- Address 1
       |
       +---- Address 2
       |
       +---- Address 3

The address types currently supported are:

    PERMANENT
    CURRENT
    CORRESPONDENCE

---

## Course Management Flow

    Admin
      |
      v
    Create Course
      |
      v
    Validate Course
      |
      v
    Course Repository
      |
      v
    H2 Database

Courses can subsequently be assigned to students.

    Student
       |
       +------ Course 1
       |
       +------ Course 2
       |
       +------ Course 3

The student-course relationship is implemented using a many-to-many relationship.

---

## Database

The project uses an **H2 in-memory database**.

No external database installation is required.

The database configuration is available in:

    src/main/resources/application.properties

Example configuration:

    spring.datasource.url=jdbc:h2:mem:studentdb
    spring.datasource.username=anshu
    spring.datasource.password=password

The exact values should always be taken from the project's `application.properties`.

---

## H2 Console

The H2 database can be viewed using the H2 web console.

Start the application and open:

    http://localhost:8080/h2-console

Use the database configuration present in `application.properties`.

For the example configuration above:

    JDBC URL : jdbc:h2:mem:studentdb
    Username : sa
    Password :

After connecting, the database tables can be viewed and SQL queries can be executed directly from the H2 console.

Example queries:

    SHOW TABLES;

    SELECT * FROM STUDENT;

    SELECT * FROM ADDRESS;

    SELECT * FROM COURSE;

    SELECT * FROM STUDENT_COURSES;

---

## Application Setup

### Prerequisites

Install the following:

    Java 21
    Maven
    Git

Verify the installations:

    java -version
    mvn -version
    git --version

---

## Clone the Project

Clone the repository:

    git clone <YOUR_GITHUB_REPOSITORY_URL>

Navigate to the project:

    cd student-management-system

---

## Configure the Application

Open:

    src/main/resources/application.properties

Verify the following configurations:

- Database configuration
- JWT configuration
- Server port
- H2 console configuration

No external database is required.

---

## Build the Project

Run:

    mvn clean install

If the build is successful, start the application using:

    mvn spring-boot:run

Alternatively, the application can be started directly from IntelliJ IDEA by running:

    StudentManagementSystemApplication

---

## Application URLs

Once the application is running:

    Application
    http://localhost:8080

    H2 Console
    http://localhost:8080/h2-console

    Swagger UI
    http://localhost:8080/swagger-ui/index.html

---

## API Testing

API documentation and testing are available through Swagger/OpenAPI.

Swagger provides an interactive interface to view and execute the available APIs.

A separate Postman collection is also provided with the project for API testing and complete request examples.

Detailed API request and response documentation is intentionally maintained in Swagger and Postman rather than duplicated in this README.

---

## Database Relationships

The main entities are:

    User
     |
     |-- Administrator


    Student
     |
     |-- Address (One-to-Many)
     |
     |-- Course (Many-to-Many)


    Course
     |
     |-- Students

The major database structure is:

    USER
     |
     +-- Administrator


    STUDENT
     |
     +-- ADDRESS
     |
     +-- STUDENT_COURSES
                  |
                  +-- COURSE

---

## Exception Handling

The application uses centralized exception handling.

    Controller
        |
        v
    Service
        |
        v
    Business Validation
        |
        +---- Success ----> Response
        |
        +---- Exception
                 |
                 v
          Exception Handler
                 |
                 v
          Standard Error Response

Custom validation exceptions are used for business-specific validation failures.

---

## AOP

Spring AOP is used for cross-cutting functionality, particularly student validation.

Instead of implementing the same validation code in every student API, the validation is centralized.

    Student API 1 ----\
                        \
    Student API 2 ------> AOP Validation
                        /
    Student API 3 ----/
                      \
    Student API 4 -----\

This improves code reuse and keeps the controllers focused on their primary responsibilities.

---

## Important Notes

- The application uses an H2 in-memory database.
- No MySQL or PostgreSQL installation is required.
- Database data is available while the application is running.
- Initial data can be loaded using `data.sql`.
- JWT is used for administrator authentication.
- Student APIs use student code and date of birth validation.
- Swagger is available for API exploration.
- Postman collection is provided for API testing.
- All application configuration is maintained in `application.properties`.

---

## Quick Start

    1. Clone the repository
           |
           v
    2. Open the project
           |
           v
    3. Verify application.properties
           |
           v
    4. Run: mvn clean install
           |
           v
    5. Run: mvn spring-boot:run
           |
           v
    6. Initial data is loaded
           |
           v
    7. Use Swagger / Postman
           |
           v
    8. Use H2 Console to inspect database

---

## Author

**Anshu Singh**

Java / Spring Boot Developer
