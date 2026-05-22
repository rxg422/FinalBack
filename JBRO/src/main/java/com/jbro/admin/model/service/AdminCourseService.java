package com.jbro.admin.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jbro.admin.model.dto.AdminCourseDetailDto;
import com.jbro.admin.model.dto.AdminCourseListDto;
import com.jbro.admin.model.dto.AdminCoursePlaceSearchDto;
import com.jbro.admin.model.dto.AdminCourseSaveDto;

public interface AdminCourseService {
    List<AdminCourseListDto> getCourseList();
    AdminCourseDetailDto getCourseDetail(Long courseId);
    List<AdminCoursePlaceSearchDto> searchPlaces(String keyword, Integer categoryId);
    Long registerCourse(AdminCourseSaveDto dto);
    void updateCourse(Long courseId, AdminCourseSaveDto dto);
    void activateCourse(Long courseId);
    void inactivateCourse(Long courseId);
    List<Map<String, Object>> getLdongList();
    String uploadCourseImage(MultipartFile image);
}