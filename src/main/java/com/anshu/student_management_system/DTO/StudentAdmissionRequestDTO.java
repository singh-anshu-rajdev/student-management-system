package com.anshu.student_management_system.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentAdmissionRequestDTO {

    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String studentCode;
    private String email;
    private String mobileNumber;
    private String parentsNames;
    private List<AddressRequestDTO> addresses;
}
