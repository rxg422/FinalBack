package com.jbro.admin.model.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminReport {
    private Long reportId;
    private Long userId;
    private Long reviewId;
    private String reportType;
    private String reason;
    private LocalDateTime createdAt;
    private String status;
    private LocalDateTime handledAt;
}