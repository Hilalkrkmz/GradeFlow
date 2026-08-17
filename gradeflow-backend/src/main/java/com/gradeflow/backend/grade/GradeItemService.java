package com.gradeflow.backend.grade;

import com.gradeflow.backend.course.Course;
import com.gradeflow.backend.course.CourseRepository;
import com.gradeflow.backend.grade.dto.GradeItemRequest;
import com.gradeflow.backend.grade.dto.GradeItemResponse;
import com.gradeflow.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeItemService {

    private final GradeItemRepository gradeItemRepository;
    private final CourseRepository courseRepository;

    public List<GradeItemResponse> getAllForCourse(User user, UUID courseId) {
        Course course = findOwnedCourse(user, courseId);
        return gradeItemRepository.findByCourseId(course.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GradeItemResponse create(User user, UUID courseId, GradeItemRequest request) {
        Course course = findOwnedCourse(user, courseId);
        GradeItem item = GradeItem.builder()
                .course(course)
                .type(request.type())
                .name(request.name())
                .weightPercent(request.weightPercent())
                .score(request.score())
                .build();
        return toResponse(gradeItemRepository.save(item));
    }

    @Transactional
    public GradeItemResponse update(User user, UUID itemId, GradeItemRequest request) {
        GradeItem item = findOwnedItem(user, itemId);
        item.setType(request.type());
        item.setName(request.name());
        item.setWeightPercent(request.weightPercent());
        item.setScore(request.score());
        return toResponse(gradeItemRepository.save(item));
    }

    @Transactional
    public void delete(User user, UUID itemId) {
        gradeItemRepository.delete(findOwnedItem(user, itemId));
    }

    private GradeItem findOwnedItem(User user, UUID itemId) {
        GradeItem item = gradeItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Grade item not found"));
        if (!item.getCourse().getSemester().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Grade item not found");
        }
        return item;
    }

    private Course findOwnedCourse(User user, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!course.getSemester().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Course not found");
        }
        return course;
    }

    private GradeItemResponse toResponse(GradeItem item) {
        return new GradeItemResponse(
                item.getId(),
                item.getCourse().getId(),
                item.getType(),
                item.getName(),
                item.getWeightPercent(),
                item.getScore()
        );
    }
}