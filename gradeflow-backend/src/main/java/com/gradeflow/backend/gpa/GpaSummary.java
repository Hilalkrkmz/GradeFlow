package com.gradeflow.backend.gpa;

public record GpaSummary(
        Double gpa,
        double totalCredits,
        int completedCourseCount
) {}