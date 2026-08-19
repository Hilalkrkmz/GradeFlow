package com.gradeflow.backend.dashboard;

import com.gradeflow.backend.gpa.GpaSummary;

import java.util.UUID;

public record DashboardResponse(
        UUID currentSemesterId,
        String currentSemesterName,
        int currentSemesterCourseCount,
        GpaSummary currentSemesterGpa,
        GpaSummary overallGpa
) {}