package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.*;
import java.util.List;
import java.util.Map;

public interface AdminCourseDao {
    List<AdminCourseListDto> selectCourseList();
    AdminCourseDetailDto selectCourseDetail(Long courseId);
    List<AdminCourseDayDto> selectCourseDays(Long courseId);
    List<AdminCoursePlaceDto> selectCoursePlaces(Long courseDayId);
    List<String> selectCourseTips(Long courseId);
    List<AdminCoursePlaceSearchDto> searchPlaces(Map<String, Object> param);

    int insertCourse(AdminCourseSaveDto dto);
    int insertCourseDay(Map<String, Object> param);
    int insertCoursePlace(Map<String, Object> param);
    int insertCourseTip(Map<String, Object> param);

    int updateCourse(Map<String, Object> param);
    int updateCourseActive(Long courseId);
    int updateCourseInactive(Long courseId);

    int deleteCourseTips(Long courseId);
    int deleteCoursePlaces(Long courseId);
    int deleteCourseDays(Long courseId);
    List<Map<String, Object>> selectLdongList();
}