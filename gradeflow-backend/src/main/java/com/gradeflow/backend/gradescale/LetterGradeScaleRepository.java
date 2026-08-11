package com.gradeflow.backend.gradescale;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LetterGradeScaleRepository extends JpaRepository<LetterGradeScale, UUID> {
    List<LetterGradeScale> findAllByOrderByMinScoreDesc();
}