package com.anshu.student_management_system.Controller;

import com.anshu.student_management_system.Annotations.ValidateStudent;
import com.anshu.student_management_system.DTO.CourseResponseDTO;
import com.anshu.student_management_system.DTO.StudentProfileUpdateDTO;
import com.anshu.student_management_system.DTO.StudentResponseDTO;
import com.anshu.student_management_system.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@ValidateStudent
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PutMapping("/profile")
    public ResponseEntity<StudentResponseDTO> updateProfile(@RequestBody StudentProfileUpdateDTO request) {
        return new ResponseEntity<>(studentService.updateProfile(request), HttpStatus.OK);
    }

    @GetMapping("/courses/search")
    public ResponseEntity<List<CourseResponseDTO>> searchCourses(@RequestParam String topic) {
        return new ResponseEntity<>(studentService.searchCourses(topic), HttpStatus.OK);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<String> leaveCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(studentService.leaveCourse(courseId), HttpStatus.OK);
    }

}
