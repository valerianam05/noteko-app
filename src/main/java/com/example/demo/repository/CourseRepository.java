package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.enums.Parcours;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, UUID> {

  List<Course> findBySemesterId(UUID semesterId);

  List<Course> findByParcours(Parcours parcours);

  List<Course> findByParcoursAndSemesterId(String parcours, UUID semesterId);
}
