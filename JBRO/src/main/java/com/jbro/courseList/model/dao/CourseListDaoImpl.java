package com.jbro.courseList.model.dao;

import com.jbro.courseList.model.vo.CourseListVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseListDaoImpl implements CourseListDao {

    private final SqlSession sqlSession;
    private static final String NS = "courseListMapper.";

    @Override
    public List<CourseListVo> selectCourseList() {
        return sqlSession.selectList(NS + "selectCourseList");
    }

    @Override
    public List<String> selectPlaceTitles(Long courseId) {
        return sqlSession.selectList(NS + "selectPlaceTitles", courseId);
    }
}