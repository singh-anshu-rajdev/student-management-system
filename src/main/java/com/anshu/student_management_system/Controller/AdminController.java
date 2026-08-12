package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.DTO.LoginRequestDTO;
import com.anshu.student_management_system.DTO.LoginResponseDTO;
import com.anshu.student_management_system.DTO.RegistrationRequestDTO;
import com.anshu.student_management_system.DTO.RegistrationResponseDTO;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Service.JwtService;
import com.anshu.student_management_system.Service.ServiceImpl.AdminServiceImpl;
import com.anshu.student_management_system.Service.ServiceImpl.UserEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO registerDTO){
        return new ResponseEntity<>(adminService.registerUser(registerDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(),loginRequestDTO.getPassword()));
        return new ResponseEntity<>( jwtService.login((UserDetails) authentication.getPrincipal()), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> generateTokenFromRefreshToken(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(),loginRequestDTO.getPassword()));
        return new ResponseEntity<>( jwtService.generateTokenFromRefreshToken((UserDetails) authentication.getPrincipal(), loginRequestDTO.getRefreshToken()), HttpStatus.OK);
    }
}
