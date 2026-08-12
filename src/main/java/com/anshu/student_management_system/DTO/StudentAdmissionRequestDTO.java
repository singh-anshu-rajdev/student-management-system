package com.anshu.student_management_system.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentAdmissionRequestDTO {

    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String studentCode;
}
