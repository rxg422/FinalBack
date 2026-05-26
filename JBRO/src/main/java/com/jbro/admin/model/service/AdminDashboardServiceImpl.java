package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminDashboardDao;
import com.jbro.admin.model.dto.AdminDashboardDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminDashboardDao adminDashboardDao;
    private final Map<LocalDate, Set<String>> dailyVisitors = new ConcurrentHashMap<>();

    public AdminDashboardServiceImpl(AdminDashboardDao adminDashboardDao) {
        this.adminDashboardDao = adminDashboardDao;
    }

    @Override
    public void recordVisit(String identifier) {
        LocalDate today = LocalDate.now();
        dailyVisitors
            .computeIfAbsent(today, k -> ConcurrentHashMap.newKeySet())
            .add(identifier);
        dailyVisitors.keySet().removeIf(date -> date.isBefore(today));
    }

    @Override
    public AdminDashboardDto getDashboard() {
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setPendingReports(adminDashboardDao.selectPendingReportCount());
        dto.setTotalCourses(adminDashboardDao.selectTotalCourseCount());
        dto.setTodaySignups(adminDashboardDao.selectTodaySignupCount());
        dto.setTodayVisitors(
            Optional.ofNullable(dailyVisitors.get(LocalDate.now()))
                .map(Set::size).orElse(0)
        );
        dto.setRecentReports(adminDashboardDao.selectRecentReports());
        return dto;
    }
}