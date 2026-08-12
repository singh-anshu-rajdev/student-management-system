package com.anshu.student_management_system.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileUpdateDTO {

    private String email;
    private String mobileNumber;
    private String parentsNames;
    private String address;
}
