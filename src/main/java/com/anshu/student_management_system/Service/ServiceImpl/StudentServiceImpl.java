package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.AddressRequestDTO;
import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;
import com.anshu.student_management_system.Entities.Address;
import com.anshu.student_management_system.Entities.Course;
import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.ExceptionHandler.ErrorCode;
import com.anshu.student_management_system.Repositories.AddressRepository;
import com.anshu.student_management_system.Repositories.CourseRepository;
import com.anshu.student_management_system.Repositories.StudentRepository;
import com.anshu.student_management_system.Service.StudentService;
import com.anshu.student_management_system.Utilities.AddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.anshu.student_management_system.ExceptionHandler.ErrorCode.ERR_AP_2007;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public StudentResponseDTO updateProfile(String studentCode, LocalDate dateOfBirth, StudentProfileUpdateDTO request) {

        Student student = getLoggedInStudent(studentCode);

        student.setName(request.getName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setEmail(request.getEmail());
        student.setMobileNumber(request.getMobileNumber());
        student.setParentsNames(request.getParentsNames());

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

        response = updateAddress(savedStudent, request, response);

        response.setStatusMessage("Profile updated successfully");

        return response;
    }

    private StudentResponseDTO updateAddress(Student savedStudent, StudentProfileUpdateDTO request, StudentResponseDTO response) {
        List<AddressRequestDTO> updatedAddresses = new ArrayList<>();

        if (request.getAddress() != null) {

            request.getAddress().forEach(addressRequest -> {

                Optional<Address> address = savedStudent.getAddresses()
                        .stream()
                        .filter(a -> a.getId().equals(addressRequest.getId()))
                        .findFirst();

                if (address.isPresent()) {

                    Address existingAddress = address.get();

                    existingAddress.setAddressType(
                            AddressType.valueOf(addressRequest.getAddressType())
                    );
                    existingAddress.setAddressLine(addressRequest.getAddressLine());
                    existingAddress.setCity(addressRequest.getCity());
                    existingAddress.setState(addressRequest.getState());
                    existingAddress.setPostalCode(addressRequest.getPostalCode());

                    addressRepository.save(existingAddress);

                    AddressRequestDTO addressResponse = new AddressRequestDTO();

                    addressResponse.setId(existingAddress.getId());
                    addressResponse.setAddressType(existingAddress.getAddressType().name());
                    addressResponse.setAddressLine(existingAddress.getAddressLine());
                    addressResponse.setCity(existingAddress.getCity());
                    addressResponse.setState(existingAddress.getState());
                    addressResponse.setPostalCode(existingAddress.getPostalCode());

                    updatedAddresses.add(addressResponse);
                }
            });
        }

        response.setAddressRequestDTOList(updatedAddresses);
        return response;
    }

    private StudentResponseDTO updateAddressResponse(Student savedStudent, StudentProfileUpdateDTO request, StudentResponseDTO response) {

        List<AddressRequestDTO> updatedAddresses = new ArrayList<>();
        if (request.getAddress() != null) {
            request.getAddress().forEach(addressRequest -> {
                Optional<Address> address = savedStudent.getAddresses()
                        .stream()
                        .filter(a -> a.getId().equals(addressRequest.getId()))
                        .findFirst();

                if (address.isPresent()) {

                    Address existingAddress = address.get();
                    AddressRequestDTO addressResponse = new AddressRequestDTO();

                    addressResponse.setId(existingAddress.getId());
                    addressResponse.setAddressType(existingAddress.getAddressType().name());
                    addressResponse.setAddressLine(existingAddress.getAddressLine());
                    addressResponse.setCity(existingAddress.getCity());
                    addressResponse.setState(existingAddress.getState());
                    addressResponse.setPostalCode(existingAddress.getPostalCode());
                    updatedAddresses.add(addressResponse);
                }
            });
        }

        response.setAddressRequestDTOList(updatedAddresses);
        return response;
    }

    @Override
    public List<CourseResponseDTO> searchCourses(String studentCode, LocalDate dateOfBirth, String topic) {

        Student student = getLoggedInStudent(studentCode);
        return student.getCourses()
                .stream()
                .filter(course ->
                        course.getTopics() != null && course.getTopics().toLowerCase().contains(topic.toLowerCase()))
                .map(course -> {

                    CourseResponseDTO response = new CourseResponseDTO();
                    response.setCourseId(course.getId());
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
    public String leaveCourse(String studentCode, LocalDate dateOfBirth, Long courseId) {

        Student student = getLoggedInStudent(studentCode);
        boolean removed = student.getCourses().removeIf(course -> course.getId().equals(courseId));
        if (!removed) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2006);
        }
        studentRepository.save(student);
        return "Course has been successfully removed from the student's enrolled courses.";
    }

    private Student getLoggedInStudent(String studentCode) {
        return studentRepository.findByStudentCode(studentCode).orElseThrow(() ->
                        new CustomValidationException(ErrorCode.ERR_AP_2003));
    }

}