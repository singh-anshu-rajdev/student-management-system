package com.anshu.student_management_system.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponseDTO {

    private String studentCode;
    private Long courseId;
    private String courseName;
    private String description;
    private String courseType;
    private String duration;
    private String topics;
    private String statusMessage;
}
