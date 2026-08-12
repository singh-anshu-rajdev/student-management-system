package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;
import com.anshu.student_management_system.Entities.Course;
import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.ExceptionHandler.ErrorCode;
import com.anshu.student_management_system.Repositories.CourseRepository;
import com.anshu.student_management_system.Repositories.StudentRepository;
import com.anshu.student_management_system.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public StudentResponseDTO updateProfile(StudentProfileUpdateDTO request) {

        Student student = getLoggedInStudent();
        Student savedStudent = studentRepository.save(student);

        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(savedStudent.getId());
        response.setName(savedStudent.getName());
        response.setDateOfBirth(savedStudent.getDateOfBirth());
        response.setGender(savedStudent.getGender());
        response.setStudentCode(savedStudent.getStudentCode());
        response.setStatusMessage("Profile updated successfully");
        return response;
    }


    @Override
    public List<CourseResponseDTO> searchCourses(String topic) {

        Student student = getLoggedInStudent();
        return student.getCourses()
                .stream()
                .filter(course ->
                        course.getTopics() != null &&
                                course.getTopics()
                                        .toLowerCase()
                                        .contains(topic.toLowerCase())
                )
                .map(course -> {

                    CourseResponseDTO response =
                            new CourseResponseDTO();

                    response.setId(course.getId());
                    response.setCourseName(course.getCourseName());
                    response.setDescription(course.getDescription());
                    response.setCourseType(course.getCourseType());
                    response.setDuration(course.getDuration());
                    response.setTopics(course.getTopics());
                    return response;

                })
                .toList();
    }


    @Override
    public String leaveCourse(Long courseId) {

        Student student = getLoggedInStudent();
        boolean removed = student.getCourses().removeIf(course -> course.getId().equals(courseId));
        if (!removed) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2006);
        }
        studentRepository.save(student);
        return "Course has been successfully removed from the student's enrolled courses.";
    }

    private Student getLoggedInStudent() {
        String studentCode = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getName();

        return studentRepository.findByStudentCode(studentCode).orElseThrow(() ->
                        new CustomValidationException(ErrorCode.ERR_AP_2003));
    }

}