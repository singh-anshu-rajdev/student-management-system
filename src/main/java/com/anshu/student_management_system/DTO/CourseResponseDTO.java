package com.anshu.student_management_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {

    private Long id;
    private String courseName;
    private String description;
    private String courseType;
    private String duration;
    private String topics;
    private String statusMessage;
}
