package com.jbro.courseDetail.model.dao;

import com.jbro.courseDetail.model.vo.CourseDetailVo;
import com.jbro.courseDetail.model.vo.CourseDetailTipVo;
import com.jbro.courseDetail.model.vo.CoursePopularPlaceVo;

import java.util.List;

public interface CourseDetailDao {
    List<CourseDetailVo> getCourseDetail(long courseId);
    List<CourseDetailTipVo> getCourseTips(long courseId);
    List<CoursePopularPlaceVo> getPopularPlaces(int regionCode);
}