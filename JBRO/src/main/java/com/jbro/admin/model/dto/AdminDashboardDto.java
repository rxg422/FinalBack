package com.jbro.admin.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDashboardDto {
    private int pendingReports;
    private int totalCourses;
    private List<RecentReport> recentReports;
    private int todaySignups;      
    private int todayVisitors; 

    @Data
    @NoArgsConstructor
    public static class RecentReport {
        private Long reportId;
        private String reporterNickname;
        private String reportType;
        private String reason;
        private String status;
        private LocalDateTime createdAt;
    }
}