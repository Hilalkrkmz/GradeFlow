package com.gradeflow.backend.user;

import com.gradeflow.backend.auth.dto.MessageResponse;
import com.gradeflow.backend.gpa.GpaSummary;
import com.gradeflow.backend.user.dto.ChangePasswordRequest;
import com.gradeflow.backend.user.dto.UpdateProfileRequest;
import com.gradeflow.backend.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/overall-gpa")
    public ResponseEntity<GpaSummary> getOverallGpa(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getOverallGpa(user));
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getProfile(user));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(user, request));
    }

    @PutMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(user, request);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }
}