package com.gradeflow.backend.semester.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record SemesterRequest(
        @NotBlank String name,
        LocalDate startDate,
        LocalDate endDate
) {}