package com.gradeflow.backend.course;

import com.gradeflow.backend.course.dto.CourseRequest;
import com.gradeflow.backend.course.dto.CourseResponse;
import com.gradeflow.backend.semester.Semester;
import com.gradeflow.backend.semester.SemesterRepository;
import com.gradeflow.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;

    public List<CourseResponse> getAllForSemester(User user, UUID semesterId) {
        Semester semester = findOwnedSemester(user, semesterId);
        return courseRepository.findBySemesterId(semester.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse create(User user, UUID semesterId, CourseRequest request) {
        Semester semester = findOwnedSemester(user, semesterId);
        Course course = Course.builder()
                .semester(semester)
                .name(request.name())
                .code(request.code())
                .credit(request.credit())
                .ects(request.ects())
                .instructor(request.instructor())
                .courseType(request.courseType())
                .build();
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse update(User user, UUID courseId, CourseRequest request) {
        Course course = findOwnedCourse(user, courseId);
        course.setName(request.name());
        course.setCode(request.code());
        course.setCredit(request.credit());
        course.setEcts(request.ects());
        course.setInstructor(request.instructor());
        course.setCourseType(request.courseType());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(User user, UUID courseId) {
        courseRepository.delete(findOwnedCourse(user, courseId));
    }

    private Course findOwnedCourse(User user, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!course.getSemester().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Course not found");
        }
        return course;
    }

    private Semester findOwnedSemester(User user, UUID semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
        if (!semester.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Semester not found");
        }
        return semester;
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getSemester().getId(),
                course.getName(),
                course.getCode(),
                course.getCredit(),
                course.getEcts(),
                course.getInstructor(),
                course.getCourseType()
        );
    }

    public Course findOwnedCourseForCalculation(User user, UUID courseId) {
        return findOwnedCourse(user, courseId);
    }
}