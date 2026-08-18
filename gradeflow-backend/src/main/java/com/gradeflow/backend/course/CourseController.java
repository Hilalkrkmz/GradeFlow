package com.gradeflow.backend.course;

import com.gradeflow.backend.course.dto.CourseRequest;
import com.gradeflow.backend.course.dto.CourseResponse;
import com.gradeflow.backend.grade.GradeCalculationService;
import com.gradeflow.backend.grade.GradeSummary;
import com.gradeflow.backend.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final GradeCalculationService gradeCalculationService;


    @GetMapping("/api/semesters/{semesterId}/courses")
    public ResponseEntity<List<CourseResponse>> getAll(
            @AuthenticationPrincipal User user,
            @PathVariable UUID semesterId
    ) {
        return ResponseEntity.ok(courseService.getAllForSemester(user, semesterId));
    }

    @PostMapping("/api/semesters/{semesterId}/courses")
    public ResponseEntity<CourseResponse> create(
            @AuthenticationPrincipal User user,
            @PathVariable UUID semesterId,
            @Valid @RequestBody CourseRequest request
    ) {
        return ResponseEntity.ok(courseService.create(user, semesterId, request));
    }

    @PutMapping("/api/courses/{id}")
    public ResponseEntity<CourseResponse> update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody CourseRequest request
    ) {
        return ResponseEntity.ok(courseService.update(user, id, request));
    }

    @DeleteMapping("/api/courses/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        courseService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/courses/{id}/grade-summary")
    public ResponseEntity<GradeSummary> getGradeSummary(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        Course course = courseService.findOwnedCourseForCalculation(user, id);
        return ResponseEntity.ok(gradeCalculationService.calculate(course));
    }
}