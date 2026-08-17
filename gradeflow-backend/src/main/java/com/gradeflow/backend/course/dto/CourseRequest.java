package com.gradeflow.backend.course.dto;

import com.gradeflow.backend.course.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseRequest(
        @NotBlank String name,
        @NotBlank String code,
        @NotNull @Positive Double credit,
        @NotNull @Positive Double ects,
        String instructor,
        @NotNull CourseType courseType
) {}