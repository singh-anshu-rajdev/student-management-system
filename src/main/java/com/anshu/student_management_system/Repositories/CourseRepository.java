package com.anshu.student_management_system.Repositories;

import com.anshu.student_management_system.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
}
