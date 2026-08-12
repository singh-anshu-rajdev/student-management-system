package com.anshu.student_management_system.DTO;

import lombok.Data;

@Data
public class RegistrationResponseDTO {
    private Long id;
    private String username;
    private String role;
    private String statusMessage;
}
