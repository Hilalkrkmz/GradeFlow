package com.gradeflow.backend.user;

import com.gradeflow.backend.gpa.GpaSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/overall-gpa")
    public ResponseEntity<GpaSummary> getOverallGpa(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getOverallGpa(user));
    }
}