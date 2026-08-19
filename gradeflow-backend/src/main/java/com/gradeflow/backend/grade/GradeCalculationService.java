package com.gradeflow.backend.grade;

import com.gradeflow.backend.course.Course;
import com.gradeflow.backend.gradescale.LetterGradeScale;
import com.gradeflow.backend.gradescale.LetterGradeScaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeCalculationService {

    private static final double TOLERANCE = 0.01;

    private final LetterGradeScaleRepository letterGradeScaleRepository;
    private final GradeItemRepository gradeItemRepository;

    public GradeSummary calculate(Course course) {
        List<GradeItem> items = gradeItemRepository.findByCourseId(course.getId());

        Optional<GradeItem> finalItem = items.stream()
                .filter(i -> i.getType() == GradeItemType.FINAL)
                .findFirst();
        Optional<GradeItem> makeupItem = items.stream()
                .filter(i -> i.getType() == GradeItemType.MAKEUP)
                .findFirst();

        boolean makeupOverrides = makeupItem.isPresent() && makeupItem.get().getScore() != null;

        List<GradeItem> effectiveItems = items.stream()
                .filter(i -> i.getType() != GradeItemType.MAKEUP)
                .filter(i -> !(i.getType() == GradeItemType.FINAL && makeupOverrides))
                .toList();

        double totalWeight = effectiveItems.stream()
                .mapToDouble(i -> i.getWeightPercent() == null ? 0 : i.getWeightPercent())
                .sum();
        if (makeupOverrides && finalItem.isPresent() && finalItem.get().getWeightPercent() != null) {
            totalWeight += finalItem.get().getWeightPercent();
        }

        double enteredWeight = 0;
        double weightedSum = 0;

        for (GradeItem item : effectiveItems) {
            if (item.getWeightPercent() == null || item.getScore() == null) continue;
            enteredWeight += item.getWeightPercent();
            weightedSum += item.getScore() * (item.getWeightPercent() / 100.0);
        }

        if (makeupOverrides) {
            Double finalWeight = finalItem.get().getWeightPercent();
            if (finalWeight != null) {
                enteredWeight += finalWeight;
                weightedSum += makeupItem.get().getScore() * (finalWeight / 100.0);
            }
        }

        boolean isComplete = Math.abs(totalWeight - 100.0) <= TOLERANCE
                && Math.abs(enteredWeight - totalWeight) <= TOLERANCE;

        Double numericGrade = null;
        String letter = null;
        Double gpaCoefficient = null;

        if (isComplete) {
            numericGrade = round2(weightedSum);
            final double finalNumericGrade = numericGrade;
            LetterGradeScale scale = letterGradeScaleRepository.findAllByOrderByMinScoreDesc().stream()
                    .filter(s -> finalNumericGrade >= s.getMinScore() && finalNumericGrade <= s.getMaxScore())
                    .findFirst()
                    .orElse(null);
            if (scale != null) {
                letter = scale.getLetter();
                gpaCoefficient = scale.getGpaCoefficient();
            }
        }

        return new GradeSummary(numericGrade, letter, gpaCoefficient, round2(enteredWeight), round2(totalWeight), isComplete);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}