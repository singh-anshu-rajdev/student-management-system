package com.anshu.student_management_system.Repositories;

import com.anshu.student_management_system.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentCode(String studentCode);

    Optional<Student> findByStudentCodeAndDateOfBirth(String studentCode, LocalDate dateOfBirth);

    boolean existsByStudentCode(String studentCode);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByCoursesId(Long courseId);

}
