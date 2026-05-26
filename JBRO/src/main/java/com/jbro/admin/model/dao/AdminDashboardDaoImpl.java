package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminDashboardDto.RecentReport;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AdminDashboardDaoImpl implements AdminDashboardDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminDashboardMapper.";

    public AdminDashboardDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public int selectPendingReportCount() {
        return sqlSession.selectOne(NS + "selectPendingReportCount");
    }

    @Override
    public int selectTotalCourseCount() {
        return sqlSession.selectOne(NS + "selectTotalCourseCount");
    }

    @Override
    public List<RecentReport> selectRecentReports() {
        return sqlSession.selectList(NS + "selectRecentReports");
    }
}