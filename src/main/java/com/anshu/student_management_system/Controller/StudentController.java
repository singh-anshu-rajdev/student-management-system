package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.Annotations.ValidateStudent;
import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;
import com.anshu.student_management_system.Service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@ValidateStudent
@Tag(
        name = "Student APIs",
        description = "APIs used by students"
)
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(
            summary = "Update student profile",
            description = "Updates the profile details of the authenticated student"
    )
    @PutMapping("/profile/update")
    public ResponseEntity<StudentResponseDTO> updateProfile(@RequestHeader("studentCode") String studentCode
            , @RequestHeader("dateOfBirth") LocalDate dateOfBirth, @RequestBody StudentProfileUpdateDTO request) {
        return new ResponseEntity<>(studentService.updateProfile(studentCode,dateOfBirth,request), HttpStatus.OK);
    }

    @Operation(
            summary = "Search courses",
            description = "Search courses using a topic"
    )
    @GetMapping("/courses/search")
    public ResponseEntity<List<CourseResponseDTO>> searchCourses(@RequestHeader("studentCode") String studentCode
            , @RequestHeader("dateOfBirth") LocalDate dateOfBirth, @RequestParam String topic) {
        return new ResponseEntity<>(studentService.searchCourses(studentCode,dateOfBirth,topic), HttpStatus.OK);
    }

    @Operation(
            summary = "Leave a course",
            description = "Allows a student to leave an assigned course"
    )
    @DeleteMapping("leave/courses")
    public ResponseEntity<String> leaveCourse(@RequestHeader("studentCode") String studentCode
            , @RequestHeader("dateOfBirth") LocalDate dateOfBirth, @RequestParam Long courseId) {
        return new ResponseEntity<>(studentService.leaveCourse(studentCode,dateOfBirth,courseId), HttpStatus.OK);
    }

}
