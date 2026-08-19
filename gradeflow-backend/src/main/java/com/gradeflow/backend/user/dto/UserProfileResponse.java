package com.gradeflow.backend.user.dto;

import com.gradeflow.backend.user.ThemePreference;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        ThemePreference themePreference
) {}