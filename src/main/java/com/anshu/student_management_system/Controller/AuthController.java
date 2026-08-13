package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.DTO.*;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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
    @PostMapping("/admin/register")
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO registerDTO){
        return new ResponseEntity<>(adminService.registerUser(registerDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Admin Login",
            description = "Authenticates admin and generates JWT access and refresh tokens"
    )
    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponseDTO> adminLogin(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(),loginRequestDTO.getPassword()));
        return new ResponseEntity<>( jwtService.login((UserDetails) authentication.getPrincipal()), HttpStatus.OK);
    }

    @Operation(
            summary = "Student Login",
            description = "Authenticates student and generates JWT access and refresh tokens"
    )
    @PostMapping("/student/login")
    public ResponseEntity<LoginResponseDTO> studentLogin(@RequestBody StudentLoginDTO studentLoginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(studentLoginDTO.getStudentCode(), studentLoginDTO.getDateOfBirth().toString()));
        return new ResponseEntity<>(jwtService.login((UserDetails) authentication.getPrincipal()), HttpStatus.OK);
    }

    @Operation(
            summary = "Generate Access Token",
            description = "Generates a new access token using the refresh token"
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> generateTokenFromRefreshToken(@RequestHeader("refresh-token") String refreshToken){
        return new ResponseEntity<>( jwtService.generateTokenFromRefreshToken(refreshToken), HttpStatus.OK);
    }
}
