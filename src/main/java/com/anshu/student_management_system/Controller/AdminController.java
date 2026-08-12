package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.DTO.*;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(
        name = "Admin APIs",
        description = "APIs used by administrators"
)
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Operation(
            summary = "Register Admin",
            description = "Registers a new admin user"
    )
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO registerDTO){
        return new ResponseEntity<>(adminService.registerUser(registerDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Admin Login",
            description = "Authenticates admin and generates JWT access and refresh tokens"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(),loginRequestDTO.getPassword()));
        return new ResponseEntity<>( jwtService.login((UserDetails) authentication.getPrincipal()), HttpStatus.OK);
    }

    @Operation(
            summary = "Generate Access Token",
            description = "Generates a new access token using the refresh token"
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> generateTokenFromRefreshToken(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(),loginRequestDTO.getPassword()));
        return new ResponseEntity<>( jwtService.generateTokenFromRefreshToken((UserDetails) authentication.getPrincipal(), loginRequestDTO.getRefreshToken()), HttpStatus.OK);
    }


    @Operation(
            summary = "Admit a student",
            description = "Creates a new student in the system"
    )
    @PostMapping("/students")
    public ResponseEntity<StudentResponseDTO> admitStudent(@RequestBody StudentAdmissionRequestDTO request) {
        return new ResponseEntity<>(adminService.admitStudent(request),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create a course",
            description = "Creates a new course"
    )
    @PostMapping("/courses")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO request) {
        return new ResponseEntity<>(adminService.createCourse(request),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Assign Course",
            description = "Assigns a course to a student"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/courses/assign")
    public ResponseEntity<CourseResponseDTO> assignCourse(@RequestBody CourseAssignmentRequestDTO request) {
        return new ResponseEntity<>(adminService.assignCourse(request), HttpStatus.OK);
    }

    @Operation(
            summary = "Search Students",
            description = "Searches students by name"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/students/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(@RequestParam String name) {
        return new ResponseEntity<>(adminService.searchStudents(name), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Students By Course",
            description = "Returns all students assigned to a particular course"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(adminService.getStudentsByCourse(courseId), HttpStatus.OK);
    }
}
