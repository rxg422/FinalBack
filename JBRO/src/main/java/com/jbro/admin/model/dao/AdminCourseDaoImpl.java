package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class AdminCourseDaoImpl implements AdminCourseDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminCourseMapper.";

    public AdminCourseDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<AdminCourseListDto> selectCourseList() {
        return sqlSession.selectList(NS + "selectCourseList");
    }

    @Override
    public AdminCourseDetailDto selectCourseDetail(Long courseId) {
        return sqlSession.selectOne(NS + "selectCourseDetail", courseId);
    }

    @Override
    public List<AdminCourseDayDto> selectCourseDays(Long courseId) {
        return sqlSession.selectList(NS + "selectCourseDays", courseId);
    }

    @Override
    public List<AdminCoursePlaceDto> selectCoursePlaces(Long courseDayId) {
        return sqlSession.selectList(NS + "selectCoursePlaces", courseDayId);
    }

    @Override
    public List<String> selectCourseTips(Long courseId) {
        return sqlSession.selectList(NS + "selectCourseTips", courseId);
    }

    @Override
    public List<AdminCoursePlaceSearchDto> searchPlaces(Map<String, Object> param) {
        return sqlSession.selectList(NS + "searchPlaces", param);
    }
    @Override
    public int insertCourse(AdminCourseSaveDto dto) {
        return sqlSession.insert(NS + "insertCourse", dto);
    }

    @Override
    public int insertCourseDay(Map<String, Object> param) {
        return sqlSession.insert(NS + "insertCourseDay", param);
    }

    @Override
    public int insertCoursePlace(Map<String, Object> param) {
        return sqlSession.insert(NS + "insertCoursePlace", param);
    }

    @Override
    public int insertCourseTip(Map<String, Object> param) {
        return sqlSession.insert(NS + "insertCourseTip", param);
    }

    @Override
    public int updateCourse(Map<String, Object> param) {
        return sqlSession.update(NS + "updateCourse", param);
    }

    @Override
    public int updateCourseActive(Long courseId) {
        return sqlSession.update(NS + "updateCourseActive", courseId);
    }

    @Override
    public int updateCourseInactive(Long courseId) {
        return sqlSession.update(NS + "updateCourseInactive", courseId);
    }

    @Override
    public int deleteCourseTips(Long courseId) {
        return sqlSession.delete(NS + "deleteCourseTips", courseId);
    }

    @Override
    public int deleteCoursePlaces(Long courseId) {
        return sqlSession.delete(NS + "deleteCoursePlaces", courseId);
    }

    @Override
    public int deleteCourseDays(Long courseId) {
        return sqlSession.delete(NS + "deleteCourseDays", courseId);
    }
    @Override
    public List<Map<String, Object>> selectLdongList() {
        return sqlSession.selectList(NS + "selectLdongList");
    }
}