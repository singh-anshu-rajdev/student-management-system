package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.RegistrationRequestDTO;
import com.anshu.student_management_system.DTO.RegistrationResponseDTO;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.Repositories.UserEntityRepository;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Utilities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.anshu.student_management_system.ExceptionHandler.ErrorCode.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registerDTO) {
        // Check if username already exists
        if (userEntityRepository.existsByUserName(registerDTO.getUserName())) {
            throw new CustomValidationException(ERR_AP_2000);
        }

        UserEntity user = new UserEntity();

        // Never store the plain-text password
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(Roles.ADMIN.toString());
        user.setUserName(registerDTO.getUserName());

        UserEntity savedUser = userEntityRepository.save(user);

        RegistrationResponseDTO response = new RegistrationResponseDTO();

        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());
        response.setStatusMessage("User Registered Successfully");

        return response;
    }
}
