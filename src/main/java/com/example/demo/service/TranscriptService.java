package com.example.demo.service;

import com.example.demo.dto.response.GraduationStatusResponse;
import com.example.demo.dto.response.TranscriptResponse;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseValidationRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TranscriptService {

  private static final int CREDITS_REQUIRED_PER_LEVEL = 60;
  private static final int TOTAL_CREDITS_REQUIRED = 180;

  private final StudentService studentService;
  private final CourseValidationRepository courseValidationRepository;

  public TranscriptResponse getTranscript(String std, String semesterCode) {
    Student student = studentService.findByStdNumber(std);

    List<CourseValidation> validations =
        courseValidationRepository.findByStudent_Id(student.getId());

    if (semesterCode != null) {
      validations =
          validations.stream()
              .filter(v -> v.getCourse().getSemester().getCode().equalsIgnoreCase(semesterCode))
              .toList();
    }

    double generalAverage =
        validations.stream().mapToDouble(CourseValidation::getFinalAverage).average().orElse(0.0);

    int totalValidatedCredits =
        validations.stream()
            .filter(CourseValidation::getValidated)
            .mapToInt(CourseValidation::getCreditsObtained)
            .sum();

    boolean isGraduated = computeTotalValidatedCredits(student) >= TOTAL_CREDITS_REQUIRED;

    String parcours =
        student.getEnrollments() != null && !student.getEnrollments().isEmpty()
            ? student.getEnrollments().get(0).getGroup().getParcours().name()
            : null; // ⚠️ à adapter selon ta vraie relation Student -> StudentEnrollment -> Group

    return new TranscriptResponse(
        student.getStdNumber(),
        student.getFirstName() + " " + student.getLastName(),
        semesterCode,
        parcours,
        generalAverage,
        totalValidatedCredits,
        isGraduated);
  }

  public GraduationStatusResponse getGraduationStatus(String std) {
    Student student = studentService.findByStdNumber(std);

    List<CourseValidation> validations =
        courseValidationRepository.findByStudent_Id(student.getId());

    Map<String, Integer> creditsByLevel =
        validations.stream()
            .filter(CourseValidation::getValidated)
            .collect(
                Collectors.groupingBy(
                    v -> v.getCourse().getSemester().getLevel().name(),
                    Collectors.summingInt(CourseValidation::getCreditsObtained)));

    int totalValidated = creditsByLevel.values().stream().mapToInt(Integer::intValue).sum();

    return new GraduationStatusResponse(
        student.getStdNumber(),
        TOTAL_CREDITS_REQUIRED,
        totalValidated,
        creditsByLevel,
        totalValidated >= TOTAL_CREDITS_REQUIRED);
  }

  private int computeTotalValidatedCredits(Student student) {
    return courseValidationRepository.findByStudent_Id(student.getId()).stream()
        .filter(CourseValidation::getValidated)
        .mapToInt(CourseValidation::getCreditsObtained)
        .sum();
  }
}
