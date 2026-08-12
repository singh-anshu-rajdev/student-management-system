package com.anshu.student_management_system.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateCourseRequestDTO {
    List<CourseRequestDTO> createCourseRequestDTOList;
}
