package com.anshu.student_management_system.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentLoginRequestDTO {

    private String studentCode;
    private LocalDate dateOfBirth;
}
