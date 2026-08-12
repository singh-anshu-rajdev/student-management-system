package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {

    public StudentResponseDTO updateProfile(String studentCode, LocalDate dateOfBirth, StudentProfileUpdateDTO request);

    public List<CourseResponseDTO> searchCourses(String studentCode, LocalDate dateOfBirth, String topic);

    public String leaveCourse(String studentCode, LocalDate dateOfBirth, Long courseId);

}
