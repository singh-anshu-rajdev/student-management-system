package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;

import java.util.List;

public interface StudentService {

    public StudentResponseDTO updateProfile(StudentProfileUpdateDTO request);

    public List<CourseResponseDTO> searchCourses(String topic);

    public String leaveCourse(Long courseId);

}
