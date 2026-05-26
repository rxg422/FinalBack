package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminDashboardDto.RecentReport;
import java.util.List;

public interface AdminDashboardDao {
    int selectPendingReportCount();
    int selectTotalCourseCount();
    List<RecentReport> selectRecentReports();
    int selectTodaySignupCount();
}