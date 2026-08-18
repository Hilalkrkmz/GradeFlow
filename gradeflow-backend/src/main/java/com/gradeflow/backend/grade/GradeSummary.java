package com.gradeflow.backend.grade;

public record GradeSummary(
        Double numericGrade,
        String letterGrade,
        Double gpaCoefficient,
        double enteredWeightSum,
        double totalWeightSum,
        boolean isComplete
) {}