package com.jbro.courseList.model.service;

import com.jbro.courseList.model.dto.CourseListResponseDto;
import java.util.List;

public interface CourseListService {
    List<CourseListResponseDto> getCourseList();
}