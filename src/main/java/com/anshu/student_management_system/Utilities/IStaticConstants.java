package com.anshu.student_management_system.Utilities;

public class IStaticConstants {

    public static final String UNAUTHORIZED_REQUEST = "Unauthorized: Authentication token was either missing or invalid.";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String OPENAPI_TITLE = "Student Management System API";
    public static final String OPENAPI_DESCRIPTION = "REST APIs for managing students, courses, " + "student admission and course assignments.";
    public static final String OPENAPI_VERSION = "1.0.0";
    public static final String OPENAPI_SECURITY_SCHEME = "Bearer Authentication";
    public static final String OPENAPI_BEARER_FORMAT = "JWT";
    public static final String OPENAPI_SCHEME = "bearer";
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Profile updated successfully";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User Registered Successfully";
    public static final String STUDENT_REGISTERED_SUCCESSFULLY = "Student registered successfully";
    public static final String COURSE_ASSIGNED_SUCCESSFULLY = "Course assigned successfully";
    public static final String COURSES_CREATED_SUCCESSFULLY = "Courses created successfully: ";
    public static final String ALREADY_EXISTING_COURSES = ". The following courses already exist and were skipped: ";
    public static final String NO_COURSES_CREATED = "No courses were created. The following courses already exist: ";
    public static final String COURSE_CREATED_SUCCESSFULLY = "Courses created successfully: ";
    public static final String USERNAME = "username";
    public static final String LOGIN_SUCCESSFUL = "Login Successful";
    public static final String TOKEN_TYPE = "tokenType";
    public static final String REFRESH_TOKEN = "REFRESH";
    public static final String COURSE_REMOVED_SUCCESSFULLY = "Course has been successfully removed from the student's enrolled courses.";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/h2-console/**",
            "/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };
}
