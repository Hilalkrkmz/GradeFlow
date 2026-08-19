package com.gradeflow.backend.user.dto;

import com.gradeflow.backend.user.ThemePreference;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        ThemePreference themePreference
) {}