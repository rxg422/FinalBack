package com.jbro.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.admin.model.dto.AdminCourseDetailDto;
import com.jbro.admin.model.dto.AdminCourseListDto;
import com.jbro.admin.model.dto.AdminCoursePlaceSearchDto;
import com.jbro.admin.model.dto.AdminCourseSaveDto;
import com.jbro.admin.model.service.AdminCourseService;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseController {

    private final AdminCourseService adminCourseService;

    public AdminCourseController(AdminCourseService adminCourseService) {
        this.adminCourseService = adminCourseService;
    }

    @GetMapping
    public ResponseEntity<List<AdminCourseListDto>> getCourseList() {
        return ResponseEntity.ok(adminCourseService.getCourseList());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<AdminCourseDetailDto> getCourseDetail(@PathVariable Long courseId) {
        return ResponseEntity.ok(adminCourseService.getCourseDetail(courseId));
    }

    @GetMapping("/places/search")
    public ResponseEntity<List<AdminCoursePlaceSearchDto>> searchPlaces(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer categoryId
    ) {
        return ResponseEntity.ok(adminCourseService.searchPlaces(keyword, categoryId));
    }

    @PostMapping
    public ResponseEntity<Long> registerCourse(@RequestBody AdminCourseSaveDto dto) {
        return ResponseEntity.ok(adminCourseService.registerCourse(dto));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long courseId, @RequestBody AdminCourseSaveDto dto) {
        adminCourseService.updateCourse(courseId, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{courseId}/active")
    public ResponseEntity<Void> activateCourse(@PathVariable Long courseId) {
        adminCourseService.activateCourse(courseId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{courseId}/inactive")
    public ResponseEntity<Void> inactivateCourse(@PathVariable Long courseId) {
        adminCourseService.inactivateCourse(courseId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/ldong")
    public ResponseEntity<List<Map<String, Object>>> getLdongList() {
        return ResponseEntity.ok(adminCourseService.getLdongList());
    }
    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadCourseImage(
        @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = adminCourseService.uploadCourseImage(image);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}