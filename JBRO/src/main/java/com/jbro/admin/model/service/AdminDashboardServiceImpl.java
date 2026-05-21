package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminDashboardDao;
import com.jbro.admin.model.dto.AdminDashboardDto;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminDashboardDao adminDashboardDao;

    public AdminDashboardServiceImpl(AdminDashboardDao adminDashboardDao) {
        this.adminDashboardDao = adminDashboardDao;
    }

    @Override
    public AdminDashboardDto getDashboard() {
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setPendingReports(adminDashboardDao.selectPendingReportCount());
        dto.setTotalCourses(adminDashboardDao.selectTotalCourseCount());
        dto.setRecentReports(adminDashboardDao.selectRecentReports());
        return dto;
    }
}