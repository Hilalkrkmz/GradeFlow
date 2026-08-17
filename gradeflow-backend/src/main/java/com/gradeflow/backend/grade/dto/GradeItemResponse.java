package com.gradeflow.backend.grade.dto;

import com.gradeflow.backend.grade.GradeItemType;

import java.util.UUID;

public record GradeItemResponse(
        UUID id,
        UUID courseId,
        GradeItemType type,
        String name,
        Double weightPercent,
        Double score
) {}