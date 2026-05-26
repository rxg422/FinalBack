package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminReviewDetailDto;
import com.jbro.admin.model.dto.AdminReviewListDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AdminReviewDaoImpl implements AdminReviewDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminReviewMapper.";

    public AdminReviewDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<AdminReviewListDto> selectReviewList() {
        return sqlSession.selectList(NS + "selectReviewList");
    }

    @Override
    public AdminReviewDetailDto selectReviewDetail(Long reviewId) {
        return sqlSession.selectOne(NS + "selectReviewDetail", reviewId);
    }

    @Override
    public int updateReviewHide(Long reviewId) {
        return sqlSession.update(NS + "updateReviewHide", reviewId);
    }

    @Override
    public int updateReviewShow(Long reviewId) {
        return sqlSession.update(NS + "updateReviewShow", reviewId);
    }
}