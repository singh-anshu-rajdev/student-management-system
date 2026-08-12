package com.anshu.student_management_system.DTO;

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
public class StudentResponseDTO {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String studentCode;
    private List<AddressRequestDTO> addressRequestDTOList;
    private String statusMessage;
}
