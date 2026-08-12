package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.RegistrationRequestDTO;
import com.anshu.student_management_system.DTO.RegistrationResponseDTO;

public interface AdminService {

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registerDTO);
}
