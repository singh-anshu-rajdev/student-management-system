package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.*;
import com.anshu.student_management_system.Entities.Address;
import com.anshu.student_management_system.Entities.Course;
import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.Repositories.AddressRepository;
import com.anshu.student_management_system.Repositories.CourseRepository;
import com.anshu.student_management_system.Repositories.StudentRepository;
import com.anshu.student_management_system.Repositories.UserEntityRepository;
import com.anshu.student_management_system.Service.AdminService;
import com.anshu.student_management_system.Utilities.AddressType;
import com.anshu.student_management_system.Utilities.IStaticConstants;
import com.anshu.student_management_system.Utilities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.anshu.student_management_system.ExceptionHandler.ErrorCode.*;
import static com.anshu.student_management_system.Utilities.IStaticConstants.*;

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
        if(registerDTO.getRole() != null && !registerDTO.getRole().isEmpty()) {
            user.setRole(registerDTO.getRole());
        } else {
            user.setRole(Roles.ADMIN.toString());
        }
        user.setUserName(registerDTO.getUserName());

        UserEntity savedUser = userEntityRepository.save(user);

        RegistrationResponseDTO response = new RegistrationResponseDTO();

        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());
        response.setStatusMessage(USER_REGISTERED_SUCCESSFULLY);

        return response;
    }

    @Override
    @Transactional
    public StudentResponseDTO admitStudent(StudentAdmissionRequestDTO request) {

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new CustomValidationException(ERR_AP_2002);
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setStudentCode(request.getStudentCode());
        student.setEmail(request.getEmail());
        student.setMobileNumber(request.getMobileNumber());
        student.setParentsNames(request.getParentsNames());

        if (request.getAddresses() != null) {
            List<Address> addresses = request.getAddresses()
                    .stream()
                    .map(addressRequest -> {

                        Address address = new Address();
                        address.setAddressType(AddressType.valueOf(addressRequest.getAddressType()));
                        address.setAddressLine(addressRequest.getAddressLine());
                        address.setCity(addressRequest.getCity());
                        address.setState(addressRequest.getState());
                        address.setPostalCode(addressRequest.getPostalCode());
                        address.setStudent(student);
                        return address;
                    })
                    .toList();
            student.setAddresses(addresses);
        }

        Student savedStudent = studentRepository.save(student);

        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(savedStudent.getId());
        response.setName(savedStudent.getName());
        response.setDateOfBirth(savedStudent.getDateOfBirth());
        response.setGender(savedStudent.getGender());
        response.setStudentCode(savedStudent.getStudentCode());
        response.setEmail(savedStudent.getEmail());
        response.setMobileNumber(savedStudent.getMobileNumber());
        response.setParentsNames(savedStudent.getParentsNames());

        List<AddressRequestDTO> addressResponses = new ArrayList<>();

        savedStudent.getAddresses().forEach(address -> {
            AddressRequestDTO addressResponse = new AddressRequestDTO();
            addressResponse.setId(address.getId());
            addressResponse.setAddressType(address.getAddressType().toString());
            addressResponse.setAddressLine(address.getAddressLine());
            addressResponse.setCity(address.getCity());
            addressResponse.setState(address.getState());
            addressResponse.setPostalCode(address.getPostalCode());
            addressResponses.add(addressResponse);
        });

        response.setAddressRequestDTOList(addressResponses);
        response.setStatusMessage(STUDENT_REGISTERED_SUCCESSFULLY);

        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setUserName(request.getStudentCode());
        registrationRequestDTO.setPassword(request.getDateOfBirth().toString());
        registrationRequestDTO.setRole(Roles.STUDENT.toString());
        registerUser(registrationRequestDTO);

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

        response.setCourseId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setDescription(course.getDescription());
        response.setStudentCode(student.getStudentCode());
        response.setStatusMessage(COURSE_ASSIGNED_SUCCESSFULLY);

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
                    response.setEmail(student.getEmail());
                    response.setMobileNumber(student.getMobileNumber());
                    response.setParentsNames(student.getParentsNames());

                    List<AddressRequestDTO> addressResponses = new ArrayList<>();
                    student.getAddresses().forEach(address -> {
                        AddressRequestDTO addressResponse = new AddressRequestDTO();
                        addressResponse.setId(address.getId());
                        addressResponse.setAddressType(address.getAddressType().toString());
                        addressResponse.setAddressLine(address.getAddressLine());
                        addressResponse.setCity(address.getCity());
                        addressResponse.setState(address.getState());
                        addressResponse.setPostalCode(address.getPostalCode());
                        addressResponses.add(addressResponse);
                    });
                    response.setAddressRequestDTOList(addressResponses);

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
                    response.setEmail(student.getEmail());
                    response.setMobileNumber(student.getMobileNumber());
                    response.setParentsNames(student.getParentsNames());

                    return response;

                })
                .toList();
    }

    @Override
    public CourseResponseDTO createCourse(CreateCourseRequestDTO request) {

        List<String> alreadyExistingCourses = new ArrayList<>();
        List<Course> coursesToCreate = new ArrayList<>();

        request.getCreateCourseRequestDTOList()
                .forEach(createCourseRequestDTO -> {

                    if (courseRepository.existsByCourseName(createCourseRequestDTO.getCourseName())) {
                        alreadyExistingCourses.add(createCourseRequestDTO.getCourseName());
                    } else {
                        Course course = new Course();
                        course.setCourseName(createCourseRequestDTO.getCourseName());
                        course.setDescription(createCourseRequestDTO.getDescription());
                        course.setCourseType(createCourseRequestDTO.getCourseType());
                        course.setDuration(createCourseRequestDTO.getDuration());
                        course.setTopics(createCourseRequestDTO.getTopics());
                        coursesToCreate.add(course);
                    }
                });


        List<Course> savedCourses = courseRepository.saveAll(coursesToCreate);
        List<String> createdCourses = savedCourses.stream()
                .map(Course::getCourseName)
                .toList();

        CourseResponseDTO response = new CourseResponseDTO();

        response.setCourseName(String.join(", ", createdCourses));

        if (!alreadyExistingCourses.isEmpty() && !createdCourses.isEmpty()) {
            response.setStatusMessage(
                    COURSES_CREATED_SUCCESSFULLY
                            + String.join(", ", createdCourses)
                            + ALREADY_EXISTING_COURSES
                            + String.join(", ", alreadyExistingCourses)
            );
        } else if (!alreadyExistingCourses.isEmpty()) {
            response.setStatusMessage(
                    NO_COURSES_CREATED
                            + String.join(", ", alreadyExistingCourses)
            );
        } else {
            response.setStatusMessage(
                    COURSE_CREATED_SUCCESSFULLY
                            + String.join(", ", createdCourses)
            );
        }
        return response;
    }
}
