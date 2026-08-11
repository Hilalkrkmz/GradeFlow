package com.gradeflow.backend.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findBySemesterId(UUID semesterId);
}