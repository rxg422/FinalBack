package com.jbro.admin.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminReportListDto {
    private Long reportId;
    private Long reviewId;
    private String reporterNickname;
    private String targetNickname;
    private String placeTitle;
    private String reportType;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
}