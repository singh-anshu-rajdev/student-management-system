package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.DTO.*;
import com.anshu.student_management_system.Service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(
        name = "Admin APIs",
        description = "APIs used by administrators"
)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Operation(
            summary = "Admit a student",
            description = "Creates a new student in the system"
    )
    @PostMapping("/admitStudents")
    public ResponseEntity<StudentResponseDTO> admitStudent(@RequestBody StudentAdmissionRequestDTO request) {
        return new ResponseEntity<>(adminService.admitStudent(request),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create a course",
            description = "Creates a new course"
    )
    @PostMapping("/createCourses")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CreateCourseRequestDTO request) {
        return new ResponseEntity<>(adminService.createCourse(request),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Assign Course",
            description = "Assigns a course to a student"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/courses/assign")
    public ResponseEntity<CourseResponseDTO> assignCourse(@RequestBody CourseAssignmentRequestDTO request) {
        return new ResponseEntity<>(adminService.assignCourse(request), HttpStatus.OK);
    }

    @Operation(
            summary = "Search Students",
            description = "Searches students by name"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/students/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(@RequestParam String name) {
        return new ResponseEntity<>(adminService.searchStudents(name), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Students By Course",
            description = "Returns all students assigned to a particular course"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/courses/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCourse(@RequestParam Long courseId) {
        return new ResponseEntity<>(adminService.getStudentsByCourse(courseId), HttpStatus.OK);
    }
}
