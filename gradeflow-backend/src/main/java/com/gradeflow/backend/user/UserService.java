package com.gradeflow.backend.user;

import com.gradeflow.backend.gpa.GpaService;
import com.gradeflow.backend.gpa.GpaSummary;
import com.gradeflow.backend.semester.Semester;
import com.gradeflow.backend.semester.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SemesterRepository semesterRepository;
    private final GpaService gpaService;

    public GpaSummary getOverallGpa(User user) {
        List<Semester> semesters = semesterRepository.findByUserIdOrderByStartDateDesc(user.getId());
        return gpaService.calculateOverall(semesters);
    }
}