package com.jbro.admin.controller;

import com.jbro.admin.model.dto.AdminDashboardDto;
import com.jbro.admin.model.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping
    public ResponseEntity<AdminDashboardDto> getDashboard() {
        return ResponseEntity.ok(adminDashboardService.getDashboard());
    }
}