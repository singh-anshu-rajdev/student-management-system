package com.anshu.student_management_system.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false, length = 150)
    private String courseName;

    @Column(length = 500)
    private String description;

    @Column(name = "course_type", nullable = false, length = 50)
    private String courseType;

    @Column(length = 50)
    private String duration;

    @Column(length = 1000)
    private String topics;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}
