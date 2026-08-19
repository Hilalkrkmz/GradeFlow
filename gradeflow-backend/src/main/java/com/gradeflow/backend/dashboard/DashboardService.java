package com.gradeflow.backend.dashboard;

import com.gradeflow.backend.gpa.GpaService;
import com.gradeflow.backend.gpa.GpaSummary;
import com.gradeflow.backend.semester.Semester;
import com.gradeflow.backend.semester.SemesterRepository;
import com.gradeflow.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SemesterRepository semesterRepository;
    private final GpaService gpaService;

    public DashboardResponse getDashboard(User user) {
        List<Semester> semesters = semesterRepository.findByUserIdOrderByStartDateDesc(user.getId());

        Semester currentSemester = semesters.stream()
                .filter(s -> !s.isArchived())
                .findFirst()
                .orElse(null);

        GpaSummary currentSemesterGpa = currentSemester != null
                ? gpaService.calculateForSemester(currentSemester)
                : new GpaSummary(null, 0, 0);

        GpaSummary overallGpa = gpaService.calculateOverall(semesters);

        return new DashboardResponse(
                currentSemester != null ? currentSemester.getId() : null,
                currentSemester != null ? currentSemester.getName() : null,
                currentSemester != null ? currentSemester.getCourses().size() : 0,
                currentSemesterGpa,
                overallGpa
        );
    }
}