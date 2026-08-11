package com.gradeflow.backend.grade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GradeItemRepository extends JpaRepository<GradeItem, UUID> {
    List<GradeItem> findByCourseId(UUID courseId);
}