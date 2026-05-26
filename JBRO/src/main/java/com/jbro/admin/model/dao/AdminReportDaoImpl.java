package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminReportDetailDto;
import com.jbro.admin.model.dto.AdminReportListDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AdminReportDaoImpl implements AdminReportDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminReportMapper.";

    public AdminReportDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<AdminReportListDto> selectReportList() {
        return sqlSession.selectList(NS + "selectReportList");
    }

    @Override
    public AdminReportDetailDto selectReportDetail(Long reportId) {
        return sqlSession.selectOne(NS + "selectReportDetail", reportId);
    }

    @Override
    public int updateReportAccept(Long reportId) {
        return sqlSession.update(NS + "updateReportAccept", reportId);
    }

    @Override
    public int updateReviewHidden(Long reviewId) {
        return sqlSession.update(NS + "updateReviewHidden", reviewId);
    }

    @Override
    public int updateReportReject(Long reportId) {
        return sqlSession.update(NS + "updateReportReject", reportId);
    }
}