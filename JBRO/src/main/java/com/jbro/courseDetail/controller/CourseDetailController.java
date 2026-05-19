package com.jbro.courseDetail.controller;

import com.jbro.courseDetail.model.dto.CourseDetailResponseDto;
import com.jbro.courseDetail.model.service.CourseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseDetailController {

    private final CourseDetailService courseDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseDetail(@PathVariable long id) {
        CourseDetailResponseDto dto = courseDetailService.getCourseDetail(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}