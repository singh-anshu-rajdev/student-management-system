package com.anshu.student_management_system.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponseDTO {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String studentCode;
    private String email;
    private String mobileNumber;
    private String parentsNames;
    private List<AddressRequestDTO> addressRequestDTOList;
    private String statusMessage;
}
