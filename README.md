# Student Management System

A Spring Boot based Student Management System developed as an interview assignment.

The application provides Admin and Student functionality with JWT authentication, role-based authorization, AOP-based student validation, H2 database persistence, Swagger/OpenAPI documentation and centralized exception handling.

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

## Project Architecture

The application follows a layered architecture.

    Client
      |
      v
    Controllers
      |
      v
    Services
      |
      v
    Repositories
      |
      v
    H2 Database

DTOs are used between the Controller and Service layers so that database entities are not directly exposed through the APIs.

---

## Project Structure

    src/main/java
    |
    +-- com.anshu.student_management_system
        |
        +-- Annotations
        |     +-- ValidateStudent
        |
        +-- Aspect
        |     +-- StudentValidationAspect
        |
        +-- Controller
        |     +-- AuthController
        |     +-- AdminController
        |     +-- StudentController
        |
        +-- DTO
        |     +-- Request DTOs
        |     +-- Response DTOs
        |
        +-- Entities
        |     +-- UserEntity
        |     +-- Student
        |     +-- Address
        |     +-- Course
        |
        +-- ExceptionHandler
        |     +-- CustomValidationException
        |     +-- GlobalExceptionHandler
        |     +-- ErrorCode
        |
        +-- Repositories
        |     +-- UserEntityRepository
        |     +-- StudentRepository
        |     +-- AddressRepository
        |     +-- CourseRepository
        |
        +-- Service
        |     +-- AuthService
        |     +-- AdminService
        |     +-- StudentService
        |     +-- JwtService
        |
        +-- ServiceImpl
              +-- AuthServiceImpl
              +-- AdminServiceImpl
              +-- StudentServiceImpl
              +-- JwtServiceImpl

---

## Application Startup Flow

    Start Spring Boot Application
                |
                v
    Load application.properties
                |
                v
    Initialize Spring Beans
                |
                +------------------+
                |                  |
                v                  v
          Spring Security       Spring AOP
                |                  |
                +--------+---------+
                         |
                         v
                  Initialize JPA
                         |
                         v
                   Initialize H2
                         |
                         v
                   Execute data.sql
                         |
                         v
                  Application Ready
                         |
                         v
                    Port 8080

---

## Authentication

The application supports authentication for both Admin and Student users.

    Authentication Controller
              |
        +-----+-----+
        |           |
        v           v
      Admin       Student
       Login        Login
        |           |
        v           v
    Username +   Student Code
    Password     + Date of Birth
        |           |
        +-----+-----+
              |
              v
        Validate User
              |
              v
        Generate JWT
              |
              v
        Return Token

---

## Admin Authentication Flow

    Admin Login Request
            |
            v
    Username + Password
            |
            v
    AuthenticationManager
            |
            v
    UserDetailsService
            |
            v
    Validate Credentials
            |
            v
    Generate JWT
            |
            v
    Admin Access Token

The Admin JWT is then supplied with subsequent protected Admin requests.

---

## Student Authentication Flow

Students authenticate using their Student Code and Date of Birth.

    Student Login Request
            |
            v
    Student Code + DOB
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

A separate student user table is not required for this authentication flow because the existing Student entity is used to validate the student.

---

## JWT Request Flow

Every protected request follows the JWT security flow.

    Client Request
          |
          | Authorization: Bearer <JWT>
          v
    JWT Authentication Filter
          |
          v
    Extract JWT
          |
          v
    Validate JWT
          |
          v
    Load UserDetails
          |
          v
    Get Authorities
          |
          v
    SecurityContext
          |
          v
    @PreAuthorize
          |
       +--+--+
       |     |
       v     v
     ADMIN STUDENT
       |     |
       v     v
    Admin  Student
     APIs   APIs

---

## Authorization

The application uses Spring Security role/authority based authorization.

Admin APIs are protected for Admin users and Student APIs are protected for Student users.

    Request
      |
      v
    JWT Validation
      |
      v
    Authentication
      |
      v
    Authority Check
      |
      +-------------------+
      |                   |
      v                   v
    ADMIN              STUDENT
      |                   |
      v                   v
    Admin APIs         Student APIs

---

## Admin Flow

    Admin Login
         |
         v
       JWT
         |
         v
    Admin APIs
         |
         +----------------------+
         |          |           |
         v          v           v
    Admit       Create       Assign
    Student     Course       Course
         |          |           |
         v          v           v
      Student    Course     Student-Course
       Table      Table      Relationship

Additional Admin operations:

- Search students
- Get students by course
- Register Admin
- Refresh JWT token

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
     +----+---------+----------------+
     |              |                |
     v              v                v
    Update        Search           Leave
    Profile       Courses          Course
     |
     v
    Update
    Address

---

## Student Profile Update Flow

    Profile Update Request
             |
             v
    JWT Authentication
             |
             v
    Student Authorization
             |
             v
    @ValidateStudent
             |
             v
    Validate Student Code
    and Date of Birth
             |
             v
    Update Student Details
             |
             +-------------------------+
             |                         |
             v                         v
          Student                   Address
          Details                   Details
             |                         |
             +------------+------------+
                          |
                          v
                    Save Changes
                          |
                          v
                  Updated Response

Student profile information includes the required student details and additional profile information implemented in the project.

---

## AOP Student Validation

Student APIs use the custom `@ValidateStudent` annotation.

The purpose of this annotation is to avoid repeating student validation logic inside every Student API.

    Student API Request
            |
            v
    JWT Authentication
            |
            v
    Student Authorization
            |
            v
    @ValidateStudent
            |
            v
    StudentValidationAspect
            |
            v
    Validate Student Code
    + Date of Birth
            |
            v
    Student Controller
            |
            v
    Student Service
            |
            v
    Student Repository

This keeps the validation logic centralized and reusable.

---

## Address Management

A Student can have multiple addresses.

Supported address types:

- PERMANENT
- CORRESPONDENCE
- CURRENT

Address relationship:

    Student
       |
       | 1
       |
       | *
       v
    Address

The Student entity maintains the relationship with Address using JPA.

Address information can be updated as part of the Student profile update functionality.

---

## Address Update Flow

    Address Update Request
             |
             v
    Read Address ID
             |
             v
    Find Address for Student
             |
        +----+----+
        |         |
        v         v
      FOUND    NOT FOUND
        |         |
        v         v
    Update      Skip
    Address     Address
        |
        v
    Save Address
        |
        v
    Add Updated Address
    to Response

If an address ID is not found for the current student, that address is skipped instead of throwing an error.

Only successfully updated addresses are returned in the response.

---

## Course Creation Flow

The Admin can create one or more courses.

    Course Request
          |
          v
    Check Course Name
          |
       +--+--+
       |     |
       v     v
    Exists  New
       |     |
       v     v
     Skip   Create
       |     |
       +--+--+
          |
          v
    Final Response

Existing course names are skipped while new course names are created.

This prevents duplicate course creation.

---

## Course Assignment Flow

    Course Assignment Request
              |
              v
        Find Student
              |
              v
        Find Course
              |
              v
    Add Course to Student
              |
              v
        Save Student
              |
              v
      Return Response

The Student-Course relationship is maintained using a Many-to-Many relationship.

---

## Leave Course Flow

    Student Request
          |
          v
    Validate Student
          |
          v
    Find Course in
    Student Courses
          |
       +--+--+
       |     |
       v     v
     FOUND  NOT FOUND
       |       |
       v       v
    Remove   Exception
    Course
       |
       v
    Save Student
       |
       v
    Success Response

---

## Database

The application uses H2 Database.

No external database installation is required.

Main entities:

- UserEntity
- Student
- Address
- Course
- Student-Course relationship

---

## H2 Database Configuration

Database configuration is maintained in:

    src/main/resources/application.properties

The application uses the H2 database configured in the project.

Typical H2 configuration contains:

    spring.datasource.url
    spring.datasource.driver-class-name
    spring.datasource.username
    spring.datasource.password

Use the exact values present in the project's `application.properties`.

---

## H2 Console

The H2 console can be accessed using:

    http://localhost:8080/h2-console

Use the JDBC URL, username and password configured in:

    src/main/resources/application.properties

For example, if the project is configured with:

    spring.datasource.url=jdbc:h2:file:./data/studentdb
    spring.datasource.username=anshu
    spring.datasource.password=password

then those same values should be entered in the H2 console.

---

## Viewing Database Data

After logging into the H2 console, the following queries can be used.

Show all tables:

    SHOW TABLES;

View users:

    SELECT * FROM USER_ENTITY;

View students:

    SELECT * FROM STUDENT;

View addresses:

    SELECT * FROM ADDRESS;

View courses:

    SELECT * FROM COURSE;

View student-course relationships:

    SELECT * FROM STUDENT_COURSES;

The exact table names can vary depending on the Hibernate/JPA naming configuration.


## Database Relationship

    +----------------------+
    |      UserEntity      |
    +----------------------+
    | id                   |
    | username             |
    | password             |
    | role                 |
    +----------------------+

    +----------------------+
    |       Student        |
    +----------------------+
    | id                   |
    | name                 |
    | dateOfBirth          |
    | gender               |
    | studentCode          |
    | email                |
    | mobileNumber         |
    | parentsNames         |
    +----------+-----------+
               |
               | 1
               |
               | *
               v
    +----------------------+
    |       Address        |
    +----------------------+
    | id                   |
    | addressType          |
    | addressLine          |
    | city                 |
    | state                |
    | postalCode           |
    | student_id           |
    +----------------------+

    +----------------------+
    |        Course        |
    +----------------------+
    | id                   |
    | courseName           |
    | description          |
    | courseType           |
    | duration             |
    | topics               |
    +----------+-----------+
               |
               | *
               |
               | *
               v
    +----------------------+
    |   student_courses    |
    +----------------------+
    | student_id           |
    | course_id            |
    +----------------------+

---

## Running the Application

### Prerequisites

Install:

- Java 21
- Maven

Verify Java installation:

    java -version

Verify Maven installation:

    mvn -version

---

## Build the Application

From the project root directory:

    mvn clean install

---

## Start the Application

Using Maven:

    mvn spring-boot:run

Alternatively, run:

    StudentManagementSystemApplication

from IntelliJ IDEA.

The application will start on the configured server port.

Default URL:

    http://localhost:8080

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
       Open Project
             |
             v
    Check application.properties
             |
             v
       mvn clean install
             |
             v
       Start Application
             |
             v
       Open Swagger
             |
             v
       Open H2 Console
             |
             v
       Authenticate
             |
             v
       Test APIs

No external database setup is required because H2 is used.

---

## Swagger / OpenAPI

Swagger is implemented for API documentation and API testing.

Swagger UI:

    http://localhost:8080/swagger-ui/index.html

OpenAPI specification:

    http://localhost:8080/v3/api-docs

Swagger provides the API documentation and allows the APIs to be tested directly from the browser.

Protected APIs require the appropriate authentication token.

---

## API Testing Flow

The recommended testing sequence is:

    Start Application
           |
           v
    Verify H2 Database
           |
           v
    Admin Login
           |
           v
    Get Admin JWT
           |
           v
    Admit Student
           |
           v
    Create Course
           |
           v
    Assign Course
           |
           v
    Student Login
           |
           v
    Get Student JWT
           |
           v
    Student APIs
           |
       +---+---+----------------+
       |       |                |
       v       v                v
    Profile  Search          Leave
    Update   Courses         Course

Detailed API request and response information is available through Swagger and the Postman collection.

---

## Exception Handling

The application uses centralized exception handling.

Application-specific validation errors are handled using:

    CustomValidationException

Error codes are maintained in:

    ErrorCode

The global exception handler handles application exceptions and returns consistent error responses.

The application also uses Spring's `ProblemDetail` mechanism for HTTP error responses.

Example:

    {
        "status": 403,
        "title": "Forbidden",
        "detail": "Access Denied"
    }

Centralized exception handling avoids implementing separate exception handling logic inside every controller.

---

## Security

The application uses Spring Security with JWT authentication.

Authentication is stateless.

    Client
      |
      | JWT
      v
    JWT Filter
      |
      v
    Validate Token
      |
      v
    SecurityContext
      |
      v
    Authorization
      |
      v
    Controller

The application does not depend on HTTP sessions for authentication.

---

## Role-Based Authorization

The application uses authorization checks on protected APIs.

Admin APIs require Admin authority.

Student APIs require Student authority.

    JWT
     |
     v
    Authentication
     |
     v
    Authority
     |
     +-------------------+
     |                   |
     v                   v
    ADMIN              STUDENT
     |                   |
     v                   v
 Admin Controller    Student Controller

Unauthorized requests are rejected by Spring Security.

---

## DTO Layer

DTOs are used to separate API models from database entities.

    Controller
        |
        v
    Request DTO
        |
        v
    Service
        |
        v
    Entity
        |
        v
    Repository
        |
        v
    Database

For responses:

    Database
        |
        v
    Entity
        |
        v
    Service
        |
        v
    Response DTO
        |
        v
    Controller
        |
        v
    Client

This prevents entities from being directly exposed through the API layer.

---

## Service Layer

Business logic is implemented in service implementation classes.

Examples:

    AdminServiceImpl
    StudentServiceImpl
    AuthServiceImpl
    JwtServiceImpl

The controllers are responsible mainly for handling HTTP requests and delegating business operations to the service layer.

---

## Repository Layer

Spring Data JPA repositories are used for database operations.

Examples:

    UserEntityRepository
    StudentRepository
    AddressRepository
    CourseRepository

The repositories provide database access while keeping database operations separate from business logic.

---

## Testing

Unit tests are implemented for the service layer.

The service tests cover scenarios such as:

- Successful student admission
- Duplicate student validation
- Successful course creation
- Duplicate course handling
- Course assignment
- Student search
- Course search
- Profile update
- Address update
- Address not found and skipped
- Course removal
- Course not enrolled validation
- Student not found validation

The tests use mocked repositories so that service business logic can be tested independently of the database.

---

## Complete Application Flow

    +------------------------------------------------------+
    |                       CLIENT                         |
    |                Swagger / Postman                    |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                  AUTHENTICATION                      |
    |                                                      |
    |   Admin Login                 Student Login          |
    |   Username + Password         Student Code + DOB     |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                    JWT TOKEN                         |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                SPRING SECURITY                       |
    |                                                      |
    |   JWT Filter -> Authentication -> Authorization      |
    +----------------------------+-------------------------+
                                 |
                    +------------+------------+
                    |                         |
                    v                         v
          +-------------------+     +-------------------+
          |   ADMIN APIs      |     |   STUDENT APIs   |
          +---------+---------+     +---------+---------+
                    |                         |
                    |                         v
                    |                +------------------+
                    |                | AOP Validation   |
                    |                | @ValidateStudent |
                    |                +--------+---------+
                    |                         |
                    +------------+------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                     CONTROLLERS                      |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                      SERVICES                        |
    |                                                      |
    | AdminServiceImpl / StudentServiceImpl / AuthService  |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                    REPOSITORIES                      |
    |                                                      |
    | Student / Address / Course / UserEntity              |
    +----------------------------+-------------------------+
                                 |
                                 v
    +------------------------------------------------------+
    |                       H2 DB                          |
    +------------------------------------------------------+

---

## Overall Features

The application implements the following major features:

- Admin registration
- Admin login
- Student login
- JWT authentication
- JWT refresh token
- Role-based authorization
- `@PreAuthorize` based access control
- Custom AOP-based student validation
- Student admission
- Student profile update
- Student address update
- Course creation
- Duplicate course handling
- Course assignment
- Course search
- Student search
- Students by course
- Leave course functionality
- H2 database
- Initial database data
- Swagger/OpenAPI documentation
- DTO based request and response handling
- Centralized exception handling
- Service-layer unit testing

---

## Final Setup Checklist

Before running the project on another machine:

    [ ] Java 21 installed
    [ ] Maven installed
    [ ] Project imported correctly
    [ ] application.properties verified
    [ ] JWT secret configured
    [ ] H2 configuration verified
    [ ] SQL initialization verified
    [ ] Maven build successful
    [ ] Application started successfully
    [ ] H2 Console accessible
    [ ] Swagger accessible
    [ ] Admin authentication verified
    [ ] Student authentication verified
    [ ] Protected APIs verified

---

## Useful URLs

Application:

    http://localhost:8080

Swagger:

    http://localhost:8080/swagger-ui/index.html

OpenAPI:

    http://localhost:8080/v3/api-docs

H2 Console:

    http://localhost:8080/h2-console

---

## Conclusion

The Student Management System is implemented using a layered Spring Boot architecture with clear separation between controllers, services, repositories and entities.

Authentication and authorization are handled using Spring Security and JWT. Student-specific validation is centralized using a custom annotation and AOP. H2 provides a lightweight database without requiring any external database installation.

Swagger and Postman can be used for API testing, while the H2 console can be used to inspect the persisted application data.
