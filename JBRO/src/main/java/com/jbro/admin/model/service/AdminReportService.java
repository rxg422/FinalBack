package com.jbro.admin.model.service;

import com.jbro.admin.model.dto.AdminReportDetailDto;
import com.jbro.admin.model.dto.AdminReportListDto;
import java.util.List;

public interface AdminReportService {
    List<AdminReportListDto> getReportList();
    AdminReportDetailDto getReportDetail(Long reportId);
    void acceptReport(Long reportId, Long reviewId);
    void rejectReport(Long reportId);
}