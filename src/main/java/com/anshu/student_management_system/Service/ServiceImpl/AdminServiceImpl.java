package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.*;
import com.anshu.student_management_system.Entities.Course;
import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.Repositories.AddressRepository;
import com.anshu.student_management_system.Repositories.CourseRepository;
import com.anshu.student_management_system.Repositories.StudentRepository;
import com.anshu.student_management_system.Repositories.UserEntityRepository;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Utilities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.anshu.student_management_system.ExceptionHandler.ErrorCode.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registerDTO) {
        // Check if username already exists
        if (userEntityRepository.existsByUserName(registerDTO.getUserName())) {
            throw new CustomValidationException(ERR_AP_2000);
        }

        UserEntity user = new UserEntity();

        // Never store the plain-text password
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(Roles.ADMIN.toString());
        user.setUserName(registerDTO.getUserName());

        UserEntity savedUser = userEntityRepository.save(user);

        RegistrationResponseDTO response = new RegistrationResponseDTO();

        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());
        response.setStatusMessage("User Registered Successfully");

        return response;
    }

    @Override
    public StudentResponseDTO admitStudent(StudentAdmissionRequestDTO request) {

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new CustomValidationException(ERR_AP_2002);
        }

        Student student = new Student();

        student.setName(request.getName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setStudentCode(request.getStudentCode());

        Student savedStudent = studentRepository.save(student);

        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(savedStudent.getId());
        response.setName(savedStudent.getName());
        response.setDateOfBirth(savedStudent.getDateOfBirth());
        response.setGender(savedStudent.getGender());
        response.setStudentCode(savedStudent.getStudentCode());
        response.setStatusMessage("Student admitted successfully");

        return response;
    }

    @Override
    public CourseResponseDTO assignCourse(CourseAssignmentRequestDTO request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new CustomValidationException(ERR_AP_2003));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new CustomValidationException(ERR_AP_2004));

        if (student.getCourses().contains(course)) {
            throw new CustomValidationException(ERR_AP_2005);
        }

        student.getCourses().add(course);
        studentRepository.save(student);

        CourseResponseDTO response = new CourseResponseDTO();

        response.setId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setDescription(course.getDescription());
        response.setCourseType(course.getCourseType());
        response.setDuration(course.getDuration());
        response.setTopics(course.getTopics());
        response.setStatusMessage("Course assigned successfully");

        return response;
    }

    @Override
    public List<StudentResponseDTO> searchStudents(String name) {

        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);

        return students.stream()
                .map(student -> {

                    StudentResponseDTO response =
                            new StudentResponseDTO();

                    response.setId(student.getId());
                    response.setName(student.getName());
                    response.setDateOfBirth(student.getDateOfBirth());
                    response.setGender(student.getGender());
                    response.setStudentCode(student.getStudentCode());

                    return response;

                })
                .toList();
    }

    @Override
    public List<StudentResponseDTO> getStudentsByCourse(Long courseId) {

        if (!courseRepository.existsById(courseId)) {
            throw new CustomValidationException(ERR_AP_2004);
        }

        List<Student> students =
                studentRepository.findByCoursesId(courseId);

        return students.stream()
                .map(student -> {

                    StudentResponseDTO response =
                            new StudentResponseDTO();

                    response.setId(student.getId());
                    response.setName(student.getName());
                    response.setDateOfBirth(student.getDateOfBirth());
                    response.setGender(student.getGender());
                    response.setStudentCode(student.getStudentCode());

                    return response;

                })
                .toList();
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO request) {

        Course course = new Course();

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setCourseType(request.getCourseType());
        course.setDuration(request.getDuration());
        course.setTopics(request.getTopics());

        Course savedCourse = courseRepository.save(course);

        CourseResponseDTO response = new CourseResponseDTO();

        response.setId(savedCourse.getId());
        response.setCourseName(savedCourse.getCourseName());
        response.setDescription(savedCourse.getDescription());
        response.setCourseType(savedCourse.getCourseType());
        response.setDuration(savedCourse.getDuration());
        response.setTopics(savedCourse.getTopics());
        response.setStatusMessage("Course created successfully");

        return response;
    }
}
