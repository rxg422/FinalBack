package com.jbro.admin.model.service;

import com.jbro.admin.model.dto.AdminDashboardDto;

public interface AdminDashboardService {
    AdminDashboardDto getDashboard();
    void recordVisit(String identifier);
}