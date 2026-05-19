package com.jbro.courseDetail.model.service;

import com.jbro.courseDetail.model.dto.CourseDetailResponseDto;

public interface CourseDetailService {
    CourseDetailResponseDto getCourseDetail(long courseId);
}