package com.example.demo.mapper;

import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getUserId(),
                student.getAppUser().getFirstName() + " " + student.getAppUser().getLastName(),
                student.getAppUser().getEmail(),
                student.getStdNumber());
    }
}