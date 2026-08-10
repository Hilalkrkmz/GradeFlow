package com.gradeflow.backend.gradescale;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "letter_grade_scale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LetterGradeScale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "min_score", nullable = false)
    private Double minScore;

    @Column(name = "max_score", nullable = false)
    private Double maxScore;

    @Column(nullable = false)
    private String letter;

    @Column(name = "gpa_coefficient", nullable = false)
    private Double gpaCoefficient;

    @Column(name = "is_passing", nullable = false)
    private boolean passing;
}