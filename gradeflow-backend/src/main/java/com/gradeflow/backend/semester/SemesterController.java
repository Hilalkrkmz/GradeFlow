package com.gradeflow.backend.semester;

import com.gradeflow.backend.semester.dto.SemesterRequest;
import com.gradeflow.backend.semester.dto.SemesterResponse;
import com.gradeflow.backend.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<List<SemesterResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(semesterService.getAllForUser(user));
    }

    @PostMapping
    public ResponseEntity<SemesterResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SemesterRequest request
    ) {
        return ResponseEntity.ok(semesterService.create(user, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemesterResponse> update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody SemesterRequest request
    ) {
        return ResponseEntity.ok(semesterService.update(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        semesterService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<SemesterResponse> archive(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return ResponseEntity.ok(semesterService.setArchived(user, id, true));
    }
}