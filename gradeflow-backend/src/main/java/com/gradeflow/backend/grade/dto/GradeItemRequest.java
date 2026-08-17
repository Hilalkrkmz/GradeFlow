package com.gradeflow.backend.grade.dto;

import com.gradeflow.backend.grade.GradeItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeItemRequest(
        @NotNull GradeItemType type,
        @NotBlank String name,
        Double weightPercent,
        Double score
) {}