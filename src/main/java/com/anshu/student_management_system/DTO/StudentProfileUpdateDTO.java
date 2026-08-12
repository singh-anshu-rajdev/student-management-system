package com.anshu.student_management_system.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentProfileUpdateDTO {

    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private List<AddressRequestDTO> address;
}
