package com.jbro.courseDetail.model.dao;

import com.jbro.courseDetail.model.vo.CourseDetailVo;
import com.jbro.courseDetail.model.vo.CourseDetailTipVo;
import com.jbro.courseDetail.model.vo.CoursePopularPlaceVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseDetailDaoImpl implements CourseDetailDao {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "com.jbro.courseDetail.model.dao.CourseDetailDao.";

    @Override
    public List<CourseDetailVo> getCourseDetail(long courseId) {
        return sqlSession.selectList(NAMESPACE + "getCourseDetail", courseId);
    }

    @Override
    public List<CourseDetailTipVo> getCourseTips(long courseId) {
        return sqlSession.selectList(NAMESPACE + "getCourseTips", courseId);
    }

    @Override
    public List<CoursePopularPlaceVo> getPopularPlaces(int regionCode) {
        return sqlSession.selectList(NAMESPACE + "getPopularPlaces", regionCode);
    }
}