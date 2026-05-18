package com.jbro.courseList.model.dao;

import com.jbro.courseList.model.vo.CourseListVo;
import java.util.List;

public interface CourseListDao {
    List<CourseListVo> selectCourseList();
    List<String> selectPlaceTitles(Long courseId);
}