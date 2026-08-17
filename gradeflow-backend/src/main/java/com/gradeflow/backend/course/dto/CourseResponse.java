package com.gradeflow.backend.course.dto;

import com.gradeflow.backend.course.CourseType;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        UUID semesterId,
        String name,
        String code,
        Double credit,
        Double ects,
        String instructor,
        CourseType courseType
) {}