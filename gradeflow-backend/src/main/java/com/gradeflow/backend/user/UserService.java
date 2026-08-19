package com.gradeflow.backend.user;

import com.gradeflow.backend.gpa.GpaService;
import com.gradeflow.backend.gpa.GpaSummary;
import com.gradeflow.backend.semester.Semester;
import com.gradeflow.backend.semester.SemesterRepository;
import com.gradeflow.backend.user.dto.ChangePasswordRequest;
import com.gradeflow.backend.user.dto.UpdateProfileRequest;
import com.gradeflow.backend.user.dto.UserProfileResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gradeflow.backend.common.exception.BadRequestException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SemesterRepository semesterRepository;
    private final GpaService gpaService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public GpaSummary getOverallGpa(User user) {
        List<Semester> semesters = semesterRepository.findByUserIdOrderByStartDateDesc(user.getId());
        return gpaService.calculateOverall(semesters);
    }

    public UserProfileResponse getProfile(User user) {
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getThemePreference());
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        if (request.themePreference() != null) {
            user.setThemePreference(request.themePreference());
        }
        userRepository.save(user);
        return getProfile(user);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}