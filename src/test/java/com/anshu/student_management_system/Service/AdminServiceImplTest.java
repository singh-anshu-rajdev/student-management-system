package com.anshu.student_management_system.Service;

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
import com.anshu.student_management_system.Service.ServiceImpl.AdminServiceImpl;
import com.anshu.student_management_system.Utilities.AddressType;
import com.anshu.student_management_system.Utilities.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private RegistrationRequestDTO registrationRequest;

    private StudentAdmissionRequestDTO studentAdmissionRequest;

    private CourseAssignmentRequestDTO courseAssignmentRequest;

    @BeforeEach
    void setUp() {

        registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setUserName("Anshu");
        registrationRequest.setPassword("anshu");

        studentAdmissionRequest = new StudentAdmissionRequestDTO();
        studentAdmissionRequest.setName("Rahul");
        studentAdmissionRequest.setDateOfBirth(LocalDate.of(2000, 1, 10));
        studentAdmissionRequest.setGender("Male");
        studentAdmissionRequest.setStudentCode("STU001");
        studentAdmissionRequest.setEmail("rahul@gmail.com");
        studentAdmissionRequest.setMobileNumber("9876543210");
        studentAdmissionRequest.setParentsNames("Rajesh Kumar");
        studentAdmissionRequest.setAddresses(new ArrayList<>());

        courseAssignmentRequest = new CourseAssignmentRequestDTO();
        courseAssignmentRequest.setStudentId(1L);
        courseAssignmentRequest.setCourseId(1L);
    }

    /**
     * registerUser()
     */

    @Test
    void registerUser_shouldRegisterUserSuccessfully() {

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setUserName("Anshu");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Roles.ADMIN.toString());

        when(userEntityRepository.existsByUserName("Anshu")).thenReturn(false);
        when(passwordEncoder.encode("anshu")).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        RegistrationResponseDTO response = adminService.registerUser(registrationRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Anshu", response.getUsername());
        assertEquals(Roles.ADMIN.toString(), response.getRole());
        assertEquals("User Registered Successfully", response.getStatusMessage());

        verify(userEntityRepository).existsByUserName("Anshu");
        verify(passwordEncoder).encode("anshu");
        verify(userEntityRepository).save(any(UserEntity.class));
    }

    @Test
    void registerUser_shouldThrowExceptionWhenUsernameAlreadyExists() {

        when(userEntityRepository.existsByUserName("Anshu")).thenReturn(true);
        assertThrows(CustomValidationException.class,
                () -> adminService.registerUser(registrationRequest)
        );
        verify(userEntityRepository).existsByUserName("Anshu");
        verify(userEntityRepository, never()).save(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerUser_shouldEncodePasswordBeforeSaving() {

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setUserName("Anshu");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Roles.ADMIN.toString());

        when(userEntityRepository.existsByUserName("Anshu")).thenReturn(false);
        when(passwordEncoder.encode("anshu")).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        adminService.registerUser(registrationRequest);

        verify(passwordEncoder).encode("anshu");

        verify(userEntityRepository)
                .save(argThat(user ->
                        user.getPassword().equals("encodedPassword")
                                && user.getRole().equals(Roles.ADMIN.toString())
                                && user.getUsername().equals("Anshu")
                ));
    }

    /**
     * admitStudent()
     */

    @Test
    void admitStudent_shouldAdmitStudentSuccessfully() {

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(false);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setName("Rahul");
        savedStudent.setDateOfBirth(LocalDate.of(2000, 1, 10));
        savedStudent.setGender("Male");
        savedStudent.setStudentCode("STU001");
        savedStudent.setEmail("rahul@gmail.com");
        savedStudent.setMobileNumber("9876543210");
        savedStudent.setParentsNames("Rajesh Kumar");
        savedStudent.setAddresses(new ArrayList<>());

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        StudentResponseDTO response = adminService.admitStudent(studentAdmissionRequest);

        assertNotNull(response);

        assertEquals(1L, response.getId());
        assertEquals("Rahul", response.getName());
        assertEquals(LocalDate.of(2000, 1, 10), response.getDateOfBirth());
        assertEquals("Male", response.getGender());
        assertEquals("STU001", response.getStudentCode());
        assertEquals("rahul@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getMobileNumber());
        assertEquals("Rajesh Kumar", response.getParentsNames());
        assertEquals("Student admitted successfully", response.getStatusMessage());

        verify(studentRepository).existsByStudentCode("STU001");

        verify(studentRepository).save(argThat(student ->
                student.getName().equals("Rahul")
                        && student.getDateOfBirth().equals(LocalDate.of(2000, 1, 10))
                        && student.getGender().equals("Male")
                        && student.getStudentCode().equals("STU001")
                        && student.getEmail().equals("rahul@gmail.com")
                        && student.getMobileNumber().equals("9876543210")
                        && student.getParentsNames().equals("Rajesh Kumar")
        ));
    }

    @Test
    void admitStudent_shouldThrowExceptionWhenStudentCodeAlreadyExists() {

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(true);

        assertThrows(CustomValidationException.class,
                () -> adminService.admitStudent(studentAdmissionRequest)
        );
        verify(studentRepository).existsByStudentCode("STU001");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void admitStudent_shouldSaveStudentWithCorrectDetails() {

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(false);

        Student savedStudent = createStudent();

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        adminService.admitStudent(studentAdmissionRequest);

        verify(studentRepository)
                .save(argThat(student ->
                        student.getName().equals("Rahul")
                                && student.getStudentCode().equals("STU001")
                                && student.getGender().equals("Male")
                                && student.getDateOfBirth()
                                .equals(LocalDate.of(2000, 1, 10))
                                && student.getEmail()
                                .equals("rahul@gmail.com")
                                && student.getMobileNumber()
                                .equals("9876543210")
                                && student.getParentsNames()
                                .equals("Rajesh Kumar")
                ));
    }

    @Test
    void admitStudent_shouldAdmitStudentWithAddresses() {

        AddressRequestDTO addressRequest = new AddressRequestDTO();

        addressRequest.setAddressType("PERMANENT");
        addressRequest.setAddressLine("123 Main Street");
        addressRequest.setCity("Bangalore");
        addressRequest.setState("Karnataka");
        addressRequest.setPostalCode("560001");

        studentAdmissionRequest.setAddresses(List.of(addressRequest));

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(false);

        Student savedStudent = createStudent();

        Address savedAddress = new Address();
        savedAddress.setId(1L);
        savedAddress.setAddressType(AddressType.PERMANENT);
        savedAddress.setAddressLine("123 Main Street");
        savedAddress.setCity("Bangalore");
        savedAddress.setState("Karnataka");
        savedAddress.setPostalCode("560001");

        savedStudent.setAddresses(List.of(savedAddress));

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        StudentResponseDTO response = adminService.admitStudent(studentAdmissionRequest);

        assertNotNull(response);
        assertNotNull(response.getAddressRequestDTOList());
        assertEquals(1, response.getAddressRequestDTOList().size());

        AddressRequestDTO addressResponse = response.getAddressRequestDTOList().get(0);

        assertEquals("PERMANENT", addressResponse.getAddressType());
        assertEquals("123 Main Street", addressResponse.getAddressLine());
        assertEquals("Bangalore", addressResponse.getCity());
        assertEquals("Karnataka", addressResponse.getState());
        assertEquals("560001", addressResponse.getPostalCode());
    }

    @Test
    void admitStudent_shouldAdmitStudentWithoutAddresses() {

        studentAdmissionRequest.setAddresses(null);

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(false);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        Student savedStudent = createStudent();
        savedStudent.setAddresses(new ArrayList<>());

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentResponseDTO response = adminService.admitStudent(studentAdmissionRequest);

        assertNotNull(response);
        assertNotNull(response.getAddressRequestDTOList());
        assertTrue(response.getAddressRequestDTOList().isEmpty());

        verify(studentRepository).save(any(Student.class));
    }

    /**
     * assignCourse()
     */

    @Test
    void assignCourse_shouldAssignCourseSuccessfully() {

        Student student = createStudent();
        student.setId(1L);
        student.setCourses(new ArrayList<>());

        Course course = createCourse();
        course.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        CourseResponseDTO response = adminService.assignCourse(courseAssignmentRequest);

        assertNotNull(response);

        assertEquals(1L, response.getCourseId());
        assertEquals("Java", response.getCourseName());
        assertEquals("Java course", response.getDescription());
        assertEquals("STU001", response.getStudentCode());

        assertEquals("Course assigned successfully", response.getStatusMessage());

        assertTrue(student.getCourses().contains(course));

        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(studentRepository).save(student);
    }

    @Test
    void assignCourse_shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class,
                () -> adminService.assignCourse(courseAssignmentRequest)
        );

        verify(studentRepository).findById(1L);
        verify(courseRepository, never()).findById(anyLong());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void assignCourse_shouldThrowExceptionWhenCourseNotFound() {

        Student student = createStudent();
        student.setId(1L);
        student.setCourses(new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class,
                () -> adminService.assignCourse(courseAssignmentRequest)
        );

        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void assignCourse_shouldThrowExceptionWhenCourseAlreadyAssigned() {

        Student student = createStudent();
        student.setId(1L);

        Course course = createCourse();
        course.setId(1L);

        student.setCourses(new ArrayList<>(List.of(course)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThrows(CustomValidationException.class,
                () -> adminService.assignCourse(courseAssignmentRequest)
        );

        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    /**
     * searchStudents()
     */

    @Test
    void searchStudents_shouldReturnMatchingStudents() {

        Student student1 = createStudent();
        student1.setId(1L);

        Student student2 = createStudent();
        student2.setId(2L);
        student2.setName("Rahul Sharma");
        student2.setStudentCode("STU002");

        when(studentRepository.findByNameContainingIgnoreCase("Rahul")).thenReturn(List.of(student1, student2));

        List<StudentResponseDTO> response = adminService.searchStudents("Rahul");

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("Rahul", response.get(0).getName());
        assertEquals("Rahul Sharma", response.get(1).getName());

        verify(studentRepository).findByNameContainingIgnoreCase("Rahul");
    }

    @Test
    void searchStudents_shouldReturnEmptyListWhenNoStudentFound() {

        when(studentRepository.findByNameContainingIgnoreCase("Unknown")).thenReturn(new ArrayList<>());

        List<StudentResponseDTO> response = adminService.searchStudents("Unknown");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(studentRepository).findByNameContainingIgnoreCase("Unknown");
    }

    @Test
    void searchStudents_shouldMapStudentDetailsCorrectly() {

        Student student = createStudent();
        student.setId(1L);

        when(studentRepository.findByNameContainingIgnoreCase("Rahul")).thenReturn(List.of(student));

        List<StudentResponseDTO> response = adminService.searchStudents("Rahul");

        assertEquals(1, response.size());

        StudentResponseDTO result = response.get(0);

        assertEquals(1L, result.getId());
        assertEquals("Rahul", result.getName());
        assertEquals(LocalDate.of(2000, 1, 10), result.getDateOfBirth());
        assertEquals("Male", result.getGender());
        assertEquals("STU001", result.getStudentCode());
        assertEquals("rahul@gmail.com", result.getEmail());
        assertEquals("9876543210", result.getMobileNumber());
        assertEquals("Rajesh Kumar", result.getParentsNames());
    }

    /**
     * getStudentsByCourse()
     */

    @Test
    void getStudentsByCourse_shouldReturnStudentsSuccessfully() {

        when(courseRepository.existsById(1L)).thenReturn(true);

        Student student1 = createStudent();
        student1.setId(1L);

        Student student2 = createStudent();
        student2.setId(2L);
        student2.setName("Amit");
        student2.setStudentCode("STU002");

        when(studentRepository.findByCoursesId(1L)).thenReturn(List.of(student1, student2));

        List<StudentResponseDTO> response = adminService.getStudentsByCourse(1L);

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("Rahul", response.get(0).getName());
        assertEquals("Amit", response.get(1).getName());

        verify(courseRepository).existsById(1L);
        verify(studentRepository).findByCoursesId(1L);
    }

    @Test
    void getStudentsByCourse_shouldThrowExceptionWhenCourseNotFound() {

        when(courseRepository.existsById(1L)).thenReturn(false);
        assertThrows(CustomValidationException.class,
                () -> adminService.getStudentsByCourse(1L)
        );

        verify(courseRepository).existsById(1L);
        verify(studentRepository, never()).findByCoursesId(anyLong());
    }

    @Test
    void getStudentsByCourse_shouldReturnEmptyListWhenNoStudentsFound() {

        when(courseRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.findByCoursesId(1L)).thenReturn(new ArrayList<>());

        List<StudentResponseDTO> response = adminService.getStudentsByCourse(1L);

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(courseRepository).existsById(1L);
        verify(studentRepository).findByCoursesId(1L);
    }

    @Test
    void getStudentsByCourse_shouldMapStudentDetailsCorrectly() {

        when(courseRepository.existsById(1L)).thenReturn(true);

        Student student = createStudent();
        student.setId(10L);

        when(studentRepository.findByCoursesId(1L)).thenReturn(List.of(student));

        List<StudentResponseDTO> response = adminService.getStudentsByCourse(1L);

        assertEquals(1, response.size());

        StudentResponseDTO result = response.get(0);

        assertEquals(10L, result.getId());
        assertEquals("Rahul", result.getName());
        assertEquals("STU001", result.getStudentCode());
        assertEquals("Male", result.getGender());
    }

    /**
     * createCourse()
     */

    @Test
    void createCourse_shouldCreateNewCoursesSuccessfully() {

        CreateCourseRequestDTO request = new CreateCourseRequestDTO();

        CourseRequestDTO courseRequest = new CourseRequestDTO();

        courseRequest.setCourseName("Java");
        courseRequest.setDescription("Java course");
        courseRequest.setCourseType("Technical");
        courseRequest.setDuration("3 Months");
        courseRequest.setTopics("Java, Spring Boot");

        request.setCreateCourseRequestDTOList(List.of(courseRequest));

        when(courseRepository.existsByCourseName("Java")).thenReturn(false);

        Course savedCourse = createCourse();

        when(courseRepository.saveAll(anyList())).thenReturn(List.of(savedCourse));

        CourseResponseDTO response = adminService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java", response.getCourseName());
        assertEquals("Courses created successfully: Java", response.getStatusMessage());

        verify(courseRepository).existsByCourseName("Java");
        verify(courseRepository).saveAll(anyList());
    }

    @Test
    void createCourse_shouldSkipExistingCourses() {

        CreateCourseRequestDTO request = new CreateCourseRequestDTO();

        CourseRequestDTO courseRequest = new CourseRequestDTO();

        courseRequest.setCourseName("Java");
        courseRequest.setDescription("Java course");
        courseRequest.setCourseType("Technical");
        courseRequest.setDuration("3 Months");
        courseRequest.setTopics("Java, Spring Boot");

        request.setCreateCourseRequestDTOList(List.of(courseRequest));

        when(courseRepository.existsByCourseName("Java")).thenReturn(true);
        when(courseRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        CourseResponseDTO response = adminService.createCourse(request);

        assertNotNull(response);
        assertEquals("", response.getCourseName());

        assertEquals("No courses were created. The following courses already exist: Java", response.getStatusMessage());

        verify(courseRepository).existsByCourseName("Java");
        verify(courseRepository).saveAll(anyList());
    }

    @Test
    void createCourse_shouldCreateNewAndSkipExistingCourses() {

        CreateCourseRequestDTO request = new CreateCourseRequestDTO();

        CourseRequestDTO javaCourse = new CourseRequestDTO();

        javaCourse.setCourseName("Java");
        javaCourse.setDescription("Java course");
        javaCourse.setCourseType("Technical");
        javaCourse.setDuration("3 Months");
        javaCourse.setTopics("Java, Spring Boot");

        CourseRequestDTO pythonCourse = new CourseRequestDTO();

        pythonCourse.setCourseName("Python");
        pythonCourse.setDescription("Python course");
        pythonCourse.setCourseType("Technical");
        pythonCourse.setDuration("2 Months");
        pythonCourse.setTopics("Python, Django");

        request.setCreateCourseRequestDTOList(List.of(javaCourse, pythonCourse));

        when(courseRepository.existsByCourseName("Java")).thenReturn(true);

        when(courseRepository.existsByCourseName("Python")).thenReturn(false);

        Course pythonSavedCourse = new Course();
        pythonSavedCourse.setId(2L);
        pythonSavedCourse.setCourseName("Python");
        pythonSavedCourse.setDescription("Python course");
        pythonSavedCourse.setCourseType("Technical");
        pythonSavedCourse.setDuration("2 Months");
        pythonSavedCourse.setTopics("Python, Django");

        when(courseRepository.saveAll(anyList())).thenReturn(List.of(pythonSavedCourse));

        CourseResponseDTO response = adminService.createCourse(request);

        assertNotNull(response);

        assertEquals("Python", response.getCourseName());

        assertEquals("Courses created successfully: Python. "
                        + "The following courses already exist and were skipped: Java",
                response.getStatusMessage()
        );

        verify(courseRepository).existsByCourseName("Java");
        verify(courseRepository).existsByCourseName("Python");
        verify(courseRepository).saveAll(argThat((List<Course> courses) ->
                        courses.size() == 1
                                && courses.get(0)
                                .getCourseName()
                                .equals("Python")
                ));
    }

    @Test
    void createCourse_shouldCreateMultipleNewCourses() {

        CreateCourseRequestDTO request = new CreateCourseRequestDTO();

        CourseRequestDTO javaCourse = new CourseRequestDTO();

        javaCourse.setCourseName("Java");
        javaCourse.setDescription("Java course");
        javaCourse.setCourseType("Technical");
        javaCourse.setDuration("3 Months");
        javaCourse.setTopics("Java, Spring Boot");

        CourseRequestDTO pythonCourse = new CourseRequestDTO();

        pythonCourse.setCourseName("Python");
        pythonCourse.setDescription("Python course");
        pythonCourse.setCourseType("Technical");
        pythonCourse.setDuration("2 Months");
        pythonCourse.setTopics("Python, Django");

        request.setCreateCourseRequestDTOList(
                List.of(javaCourse, pythonCourse)
        );

        when(courseRepository.existsByCourseName("Java")).thenReturn(false);
        when(courseRepository.existsByCourseName("Python")).thenReturn(false);

        Course javaSavedCourse = createCourse();

        Course pythonSavedCourse = new Course();
        pythonSavedCourse.setId(2L);
        pythonSavedCourse.setCourseName("Python");
        pythonSavedCourse.setDescription("Python course");
        pythonSavedCourse.setCourseType("Technical");
        pythonSavedCourse.setDuration("2 Months");
        pythonSavedCourse.setTopics("Python, Django");

        when(courseRepository.saveAll(anyList()))
                .thenReturn(
                        List.of(
                                javaSavedCourse,
                                pythonSavedCourse
                        )
                );

        CourseResponseDTO response = adminService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java, Python", response.getCourseName());
        assertEquals("Courses created successfully: Java, Python", response.getStatusMessage());

        verify(courseRepository)
                .saveAll(argThat((List<Course> courses) ->
                        courses.size() == 2
                ));
    }

    /**
     * Helper Methods
     */

    private Student createStudent() {

        Student student = new Student();

        student.setName("Rahul");
        student.setDateOfBirth(LocalDate.of(2000, 1, 10));
        student.setGender("Male");
        student.setStudentCode("STU001");
        student.setEmail("rahul@gmail.com");
        student.setMobileNumber("9876543210");
        student.setParentsNames("Rajesh Kumar");
        student.setAddresses(new ArrayList<>());
        student.setCourses(new ArrayList<>());

        return student;
    }

    private Course createCourse() {

        Course course = new Course();

        course.setId(1L);
        course.setCourseName("Java");
        course.setDescription("Java course");
        course.setCourseType("Technical");
        course.setDuration("3 Months");
        course.setTopics("Java, Spring Boot");

        return course;
    }
}
