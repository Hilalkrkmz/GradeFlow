package com.gradeflow.backend.semester;

import com.gradeflow.backend.semester.dto.SemesterRequest;
import com.gradeflow.backend.semester.dto.SemesterResponse;
import com.gradeflow.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gradeflow.backend.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public List<SemesterResponse> getAllForUser(User user) {
        return semesterRepository.findByUserIdOrderByStartDateDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SemesterResponse create(User user, SemesterRequest request) {
        Semester semester = Semester.builder()
                .user(user)
                .name(request.name())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();
        return toResponse(semesterRepository.save(semester));
    }

    @Transactional
    public SemesterResponse update(User user, UUID semesterId, SemesterRequest request) {
        Semester semester = findOwned(user, semesterId);
        semester.setName(request.name());
        semester.setStartDate(request.startDate());
        semester.setEndDate(request.endDate());
        return toResponse(semesterRepository.save(semester));
    }

    @Transactional
    public void delete(User user, UUID semesterId) {
        semesterRepository.delete(findOwned(user, semesterId));
    }

    @Transactional
    public SemesterResponse setArchived(User user, UUID semesterId, boolean archived) {
        Semester semester = findOwned(user, semesterId);
        semester.setArchived(archived);
        return toResponse(semesterRepository.save(semester));
    }

    private Semester findOwned(User user, UUID semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        if (!semester.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Semester not found");
        }
        return semester;
    }

    private SemesterResponse toResponse(Semester semester) {
        return new SemesterResponse(
                semester.getId(),
                semester.getName(),
                semester.getStartDate(),
                semester.getEndDate(),
                semester.isArchived()
        );
    }

    public Semester findOwnedForCalculation(User user, UUID semesterId) {
        return findOwned(user, semesterId);
    }
}