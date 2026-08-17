package com.gradeflow.backend.semester.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SemesterResponse(
        UUID id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean archived
) {}