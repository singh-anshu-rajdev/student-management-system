package com.anshu.student_management_system.DTO;

import lombok.Data;

@Data
public class RegistrationRequestDTO {

    private String userName;
    private String password;
    private String role;
}
