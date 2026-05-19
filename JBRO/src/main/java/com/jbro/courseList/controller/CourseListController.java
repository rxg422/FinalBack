package com.jbro.courseList.controller;

import com.jbro.courseList.model.dto.CourseListResponseDto;
import com.jbro.courseList.model.service.CourseListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourseListController {

    private final CourseListService courseListService;

    @GetMapping("/list")
    public ResponseEntity<List<CourseListResponseDto>> getCourseList() {
        return ResponseEntity.ok(courseListService.getCourseList());
    }
}