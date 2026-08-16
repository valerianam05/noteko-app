package com.example.demo.mapper;

import com.example.demo.dto.response.TeacherResponse;
import com.example.demo.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getUserId(),
                teacher.getAppUser().getFirstName() + " " + teacher.getAppUser().getLastName(),
                teacher.getAppUser().getEmail(),
                teacher.getSpecialite());
    }
}