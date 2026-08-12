package com.anshu.student_management_system.DTO;

import lombok.Data;

@Data
public class CourseRequestDTO {

    private String courseName;
    private String description;
    private String courseType;
    private String duration;
    private String topics;
}
