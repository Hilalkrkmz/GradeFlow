package com.gradeflow.backend.grade;

import com.gradeflow.backend.grade.dto.GradeItemRequest;
import com.gradeflow.backend.grade.dto.GradeItemResponse;
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
public class GradeItemController {

    private final GradeItemService gradeItemService;

    @GetMapping("/api/courses/{courseId}/grade-items")
    public ResponseEntity<List<GradeItemResponse>> getAll(
            @AuthenticationPrincipal User user,
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.ok(gradeItemService.getAllForCourse(user, courseId));
    }

    @PostMapping("/api/courses/{courseId}/grade-items")
    public ResponseEntity<GradeItemResponse> create(
            @AuthenticationPrincipal User user,
            @PathVariable UUID courseId,
            @Valid @RequestBody GradeItemRequest request
    ) {
        return ResponseEntity.ok(gradeItemService.create(user, courseId, request));
    }

    @PutMapping("/api/grade-items/{id}")
    public ResponseEntity<GradeItemResponse> update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody GradeItemRequest request
    ) {
        return ResponseEntity.ok(gradeItemService.update(user, id, request));
    }

    @DeleteMapping("/api/grade-items/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        gradeItemService.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}