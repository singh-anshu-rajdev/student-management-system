package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.*;

import java.util.List;

public interface AdminService {

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registerDTO);

    public StudentResponseDTO admitStudent(StudentAdmissionRequestDTO request);

    public CourseResponseDTO createCourse(CourseRequestDTO request);

    public CourseResponseDTO assignCourse(CourseAssignmentRequestDTO request);

    public List<StudentResponseDTO> searchStudents(String name);

    public List<StudentResponseDTO> getStudentsByCourse(Long courseId);
}
