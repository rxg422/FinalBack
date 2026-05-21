package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminReportDetailDto;
import com.jbro.admin.model.dto.AdminReportListDto;
import java.util.List;

public interface AdminReportDao {
    List<AdminReportListDto> selectReportList();
    AdminReportDetailDto selectReportDetail(Long reportId);
    int updateReportAccept(Long reportId);
    int updateReviewHidden(Long reviewId);
    int updateReportReject(Long reportId);
}