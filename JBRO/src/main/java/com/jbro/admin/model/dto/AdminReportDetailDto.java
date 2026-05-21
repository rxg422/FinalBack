package com.jbro.admin.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminReportDetailDto {
    private Long reportId;
    private Long reviewId;
    private String reporterNickname;
    private String targetNickname;
    private LocalDateTime createdAt;
    private String status;
    private LocalDateTime handledAt;
    private String reportType;
    private String reason;
    private String reviewContent;
    private String reviewImage;
}