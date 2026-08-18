package com.gradeflow.backend.gpa;

import com.gradeflow.backend.course.Course;
import com.gradeflow.backend.grade.GradeCalculationService;
import com.gradeflow.backend.grade.GradeSummary;
import com.gradeflow.backend.semester.Semester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GpaService {

    private final GradeCalculationService gradeCalculationService;

    public GpaSummary calculateForSemester(Semester semester) {
        return calculateForCourses(semester.getCourses());
    }

    public GpaSummary calculateOverall(List<Semester> semesters) {
        List<Course> allCourses = semesters.stream()
                .flatMap(s -> s.getCourses().stream())
                .toList();
        return calculateForCourses(allCourses);
    }

    private GpaSummary calculateForCourses(List<Course> courses) {
        double weightedSum = 0;
        double completedCredits = 0;
        int completedCount = 0;

        for (Course course : courses) {
            GradeSummary summary = gradeCalculationService.calculate(course);
            if (summary.isComplete() && summary.gpaCoefficient() != null && course.getCredit() != null) {
                weightedSum += summary.gpaCoefficient() * course.getCredit();
                completedCredits += course.getCredit();
                completedCount++;
            }
        }

        Double gpa = completedCredits > 0 ? round2(weightedSum / completedCredits) : null;
        return new GpaSummary(gpa, completedCredits, completedCount);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}