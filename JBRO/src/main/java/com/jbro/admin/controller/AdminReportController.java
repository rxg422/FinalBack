package com.jbro.admin.controller;

import com.jbro.admin.model.dto.AdminReportDetailDto;
import com.jbro.admin.model.dto.AdminReportListDto;
import com.jbro.admin.model.service.AdminReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReportListDto>> getReportList() {
        return ResponseEntity.ok(adminReportService.getReportList());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<AdminReportDetailDto> getReportDetail(@PathVariable Long reportId) {
        return ResponseEntity.ok(adminReportService.getReportDetail(reportId));
    }

    @PatchMapping("/{reportId}/accept")
    public ResponseEntity<Void> acceptReport(
            @PathVariable Long reportId,
            @RequestParam Long reviewId) {
        adminReportService.acceptReport(reportId, reviewId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable Long reportId) {
        adminReportService.rejectReport(reportId);
        return ResponseEntity.ok().build();
    }
}