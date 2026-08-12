package com.anshu.student_management_system.DTO;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private String refreshToken;
}
