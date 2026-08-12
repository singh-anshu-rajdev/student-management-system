package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.AddressRequestDTO;
import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;
import com.anshu.student_management_system.Entities.Address;
import com.anshu.student_management_system.Entities.Course;
import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.Repositories.AddressRepository;
import com.anshu.student_management_system.Repositories.CourseRepository;
import com.anshu.student_management_system.Repositories.StudentRepository;
import com.anshu.student_management_system.Service.ServiceImpl.StudentServiceImpl;
import com.anshu.student_management_system.Utilities.AddressType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private Course javaCourse;
    private Course pythonCourse;

    @BeforeEach
    void setUp() {

        student = new Student();
        student.setId(1L);
        student.setName("Rahul");
        student.setDateOfBirth(LocalDate.of(2000, 1, 10));
        student.setGender("Male");
        student.setStudentCode("STU001");
        student.setEmail("rahul@gmail.com");
        student.setMobileNumber("9876543210");
        student.setParentsNames("Rajesh Kumar");
        student.setAddresses(new ArrayList<>());
        student.setCourses(new ArrayList<>());

        javaCourse = new Course();
        javaCourse.setId(1L);
        javaCourse.setCourseName("Java");
        javaCourse.setDescription("Java course");
        javaCourse.setCourseType("Technical");
        javaCourse.setDuration("3 Months");
        javaCourse.setTopics("Java, Spring Boot");

        pythonCourse = new Course();
        pythonCourse.setId(2L);
        pythonCourse.setCourseName("Python");
        pythonCourse.setDescription("Python course");
        pythonCourse.setCourseType("Technical");
        pythonCourse.setDuration("2 Months");
        pythonCourse.setTopics("Python, Django");
    }

    /**
     * updateProfile()
     */

    @Test
    void updateProfile_shouldUpdateStudentProfileSuccessfully() {

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Rahul Sharma");
        request.setDateOfBirth(LocalDate.of(1999, 5, 20));
        request.setGender("Male");
        request.setEmail("rahul.sharma@gmail.com");
        request.setMobileNumber("9999999999");
        request.setParentsNames("Rajesh Sharma");
        request.setAddress(new ArrayList<>());

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO response = studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Rahul Sharma", response.getName());
        assertEquals(LocalDate.of(1999, 5, 20), response.getDateOfBirth());
        assertEquals("Male", response.getGender());
        assertEquals("STU001", response.getStudentCode());
        assertEquals("rahul.sharma@gmail.com", response.getEmail());
        assertEquals("9999999999", response.getMobileNumber());
        assertEquals("Rajesh Sharma", response.getParentsNames());
        assertEquals("Profile updated successfully", response.getStatusMessage());

        verify(studentRepository).findByStudentCode("STU001");
        verify(studentRepository).save(student);
    }

    @Test
    void updateProfile_shouldThrowExceptionWhenStudentNotFound() {

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request));

        verify(studentRepository).findByStudentCode("STU001");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void updateProfile_shouldUpdateStudentDetailsCorrectly() {

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Amit");
        request.setDateOfBirth(LocalDate.of(1998, 10, 15));
        request.setGender("Male");
        request.setEmail("amit@gmail.com");
        request.setMobileNumber("8888888888");
        request.setParentsNames("Suresh Kumar");
        request.setAddress(new ArrayList<>());

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        verify(studentRepository).save(argThat(savedStudent ->
                savedStudent.getName().equals("Amit")
                        && savedStudent.getDateOfBirth().equals(LocalDate.of(1998, 10, 15))
                        && savedStudent.getGender().equals("Male")
                        && savedStudent.getEmail().equals("amit@gmail.com")
                        && savedStudent.getMobileNumber().equals("8888888888")
                        && savedStudent.getParentsNames().equals("Suresh Kumar")
        ));
    }

    /**
     * updateProfile() - Address
     */

    @Test
    void updateProfile_shouldUpdateExistingAddress() {

        Address address = new Address();
        address.setId(10L);
        address.setAddressType(AddressType.PERMANENT);
        address.setAddressLine("Old Address");
        address.setCity("Bangalore");
        address.setState("Karnataka");
        address.setPostalCode("560001");
        address.setStudent(student);

        student.setAddresses(new ArrayList<>(List.of(address)));

        AddressRequestDTO addressRequest = new AddressRequestDTO();
        addressRequest.setId(10L);
        addressRequest.setAddressType("CURRENT");
        addressRequest.setAddressLine("New Address");
        addressRequest.setCity("Mumbai");
        addressRequest.setState("Maharashtra");
        addressRequest.setPostalCode("400001");

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Rahul");
        request.setDateOfBirth(LocalDate.of(2000, 1, 10));
        request.setGender("Male");
        request.setEmail("rahul@gmail.com");
        request.setMobileNumber("9876543210");
        request.setParentsNames("Rajesh Kumar");
        request.setAddress(new ArrayList<>(List.of(addressRequest)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        StudentResponseDTO response = studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        assertNotNull(response);
        assertNotNull(response.getAddressRequestDTOList());
        assertEquals(1, response.getAddressRequestDTOList().size());

        AddressRequestDTO updatedAddress = response.getAddressRequestDTOList().get(0);

        assertEquals(10L, updatedAddress.getId());
        assertEquals("CURRENT", updatedAddress.getAddressType());
        assertEquals("New Address", updatedAddress.getAddressLine());
        assertEquals("Mumbai", updatedAddress.getCity());
        assertEquals("Maharashtra", updatedAddress.getState());
        assertEquals("400001", updatedAddress.getPostalCode());

        verify(addressRepository).save(address);
    }

    @Test
    void updateProfile_shouldSkipAddressWhenAddressIdNotFound() {

        Address address = new Address();
        address.setId(10L);
        address.setAddressType(AddressType.PERMANENT);
        address.setAddressLine("Old Address");
        address.setCity("Bangalore");
        address.setState("Karnataka");
        address.setPostalCode("560001");

        student.setAddresses(new ArrayList<>(List.of(address)));

        AddressRequestDTO addressRequest = new AddressRequestDTO();
        addressRequest.setId(999L);
        addressRequest.setAddressType("CURRENT");
        addressRequest.setAddressLine("New Address");
        addressRequest.setCity("Mumbai");
        addressRequest.setState("Maharashtra");
        addressRequest.setPostalCode("400001");

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Rahul");
        request.setDateOfBirth(LocalDate.of(2000, 1, 10));
        request.setGender("Male");
        request.setAddress(new ArrayList<>(List.of(addressRequest)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO response = studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        assertNotNull(response);
        assertNotNull(response.getAddressRequestDTOList());
        assertTrue(response.getAddressRequestDTOList().isEmpty());

        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void updateProfile_shouldUpdateMultipleAddresses() {

        Address permanentAddress = new Address();
        permanentAddress.setId(10L);
        permanentAddress.setAddressType(AddressType.PERMANENT);
        permanentAddress.setAddressLine("Permanent Address");
        permanentAddress.setCity("Bangalore");
        permanentAddress.setState("Karnataka");
        permanentAddress.setPostalCode("560001");

        Address currentAddress = new Address();
        currentAddress.setId(20L);
        currentAddress.setAddressType(AddressType.CURRENT);
        currentAddress.setAddressLine("Current Address");
        currentAddress.setCity("Mumbai");
        currentAddress.setState("Maharashtra");
        currentAddress.setPostalCode("400001");

        student.setAddresses(new ArrayList<>(List.of(permanentAddress, currentAddress)));

        AddressRequestDTO addressRequest1 = new AddressRequestDTO();
        addressRequest1.setId(10L);
        addressRequest1.setAddressType("CORRESPONDENCE");
        addressRequest1.setAddressLine("Updated Permanent");
        addressRequest1.setCity("Delhi");
        addressRequest1.setState("Delhi");
        addressRequest1.setPostalCode("110001");

        AddressRequestDTO addressRequest2 = new AddressRequestDTO();
        addressRequest2.setId(20L);
        addressRequest2.setAddressType("CURRENT");
        addressRequest2.setAddressLine("Updated Current");
        addressRequest2.setCity("Pune");
        addressRequest2.setState("Maharashtra");
        addressRequest2.setPostalCode("411001");

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Rahul");
        request.setDateOfBirth(LocalDate.of(2000, 1, 10));
        request.setGender("Male");
        request.setAddress(new ArrayList<>(List.of(addressRequest1, addressRequest2)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponseDTO response = studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        assertNotNull(response);
        assertEquals(2, response.getAddressRequestDTOList().size());

        verify(addressRepository, times(2)).save(any(Address.class));
    }

    @Test
    void updateProfile_shouldNotUpdateAddressWhenRequestAddressIsNull() {

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setName("Rahul");
        request.setDateOfBirth(LocalDate.of(2000, 1, 10));
        request.setGender("Male");
        request.setAddress(null);

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO response = studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        assertNotNull(response);
        assertNotNull(response.getAddressRequestDTOList());
        assertTrue(response.getAddressRequestDTOList().isEmpty());

        verify(addressRepository, never()).save(any(Address.class));
    }

    /**
     * searchCourses()
     */

    @Test
    void searchCourses_shouldReturnMatchingCourses() {

        student.setCourses(new ArrayList<>(List.of(javaCourse, pythonCourse)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        List<CourseResponseDTO> response = studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "Java");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getCourseId());
        assertEquals("Java", response.get(0).getCourseName());
        assertEquals("Java course", response.get(0).getDescription());
        assertEquals("Technical", response.get(0).getCourseType());
        assertEquals("3 Months", response.get(0).getDuration());
        assertEquals("Java, Spring Boot", response.get(0).getTopics());

        verify(studentRepository).findByStudentCode("STU001");
    }

    @Test
    void searchCourses_shouldSearchCaseInsensitively() {

        student.setCourses(new ArrayList<>(List.of(javaCourse)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        List<CourseResponseDTO> response = studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "java");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Java", response.get(0).getCourseName());
    }

    @Test
    void searchCourses_shouldReturnEmptyListWhenCourseDoesNotMatch() {

        student.setCourses(new ArrayList<>(List.of(javaCourse)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        List<CourseResponseDTO> response = studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "Python");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(studentRepository).findByStudentCode("STU001");
    }

    @Test
    void searchCourses_shouldReturnEmptyListWhenStudentHasNoCourses() {

        student.setCourses(new ArrayList<>());

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        List<CourseResponseDTO> response = studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "Java");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void searchCourses_shouldIgnoreCourseWithNullTopics() {

        Course course = new Course();
        course.setId(3L);
        course.setCourseName("Testing");
        course.setTopics(null);

        student.setCourses(new ArrayList<>(List.of(javaCourse, course)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        List<CourseResponseDTO> response = studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "Java");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Java", response.get(0).getCourseName());
    }

    @Test
    void searchCourses_shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> studentService.searchCourses("STU001", LocalDate.of(2000, 1, 10), "Java"));

        verify(studentRepository).findByStudentCode("STU001");
    }

    // ============================================================
    // leaveCourse()
    // ============================================================

    @Test
    void leaveCourse_shouldRemoveCourseSuccessfully() {

        student.setCourses(new ArrayList<>(List.of(javaCourse, pythonCourse)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        String response = studentService.leaveCourse("STU001", LocalDate.of(2000, 1, 10), 1L);

        assertEquals("Course has been successfully removed from the student's enrolled courses.", response);
        assertEquals(1, student.getCourses().size());
        assertEquals("Python", student.getCourses().get(0).getCourseName());

        verify(studentRepository).findByStudentCode("STU001");
        verify(studentRepository).save(student);
    }

    @Test
    void leaveCourse_shouldThrowExceptionWhenCourseNotEnrolled() {

        student.setCourses(new ArrayList<>(List.of(javaCourse)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));

        assertThrows(CustomValidationException.class, () -> studentService.leaveCourse("STU001", LocalDate.of(2000, 1, 10), 999L));

        assertEquals(1, student.getCourses().size());

        verify(studentRepository).findByStudentCode("STU001");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void leaveCourse_shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> studentService.leaveCourse("STU001", LocalDate.of(2000, 1, 10), 1L));

        verify(studentRepository).findByStudentCode("STU001");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void leaveCourse_shouldRemoveOnlyRequestedCourse() {

        Course java = new Course();
        java.setId(1L);
        java.setCourseName("Java");

        Course python = new Course();
        python.setId(2L);
        python.setCourseName("Python");

        Course spring = new Course();
        spring.setId(3L);
        spring.setCourseName("Spring Boot");

        student.setCourses(new ArrayList<>(List.of(java, python, spring)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.leaveCourse("STU001", LocalDate.of(2000, 1, 10), 2L);

        assertEquals(2, student.getCourses().size());
        assertTrue(student.getCourses().contains(java));
        assertTrue(student.getCourses().contains(spring));
        assertFalse(student.getCourses().contains(python));

        verify(studentRepository).save(student);
    }

    /**
     * Additional verification tests
     */

    @Test
    void updateProfile_shouldSaveUpdatedAddressWithCorrectValues() {

        Address address = new Address();
        address.setId(10L);
        address.setAddressType(AddressType.PERMANENT);
        address.setAddressLine("Old Address");
        address.setCity("Bangalore");
        address.setState("Karnataka");
        address.setPostalCode("560001");

        student.setAddresses(new ArrayList<>(List.of(address)));

        AddressRequestDTO requestAddress = new AddressRequestDTO();
        requestAddress.setId(10L);
        requestAddress.setAddressType("CURRENT");
        requestAddress.setAddressLine("New Address");
        requestAddress.setCity("Mumbai");
        requestAddress.setState("Maharashtra");
        requestAddress.setPostalCode("400001");

        StudentProfileUpdateDTO request = new StudentProfileUpdateDTO();
        request.setAddress(new ArrayList<>(List.of(requestAddress)));

        when(studentRepository.findByStudentCode("STU001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.updateProfile("STU001", LocalDate.of(2000, 1, 10), request);

        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

        verify(addressRepository).save(addressCaptor.capture());

        Address savedAddress = addressCaptor.getValue();

        assertEquals(10L, savedAddress.getId());
        assertEquals(AddressType.CURRENT, savedAddress.getAddressType());
        assertEquals("New Address", savedAddress.getAddressLine());
        assertEquals("Mumbai", savedAddress.getCity());
        assertEquals("Maharashtra", savedAddress.getState());
        assertEquals("400001", savedAddress.getPostalCode());
    }
}
