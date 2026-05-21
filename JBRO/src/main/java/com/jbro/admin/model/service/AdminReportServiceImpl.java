package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminReportDao;
import com.jbro.admin.model.dto.AdminReportDetailDto;
import com.jbro.admin.model.dto.AdminReportListDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AdminReportServiceImpl implements AdminReportService {

    private final AdminReportDao adminReportDao;

    public AdminReportServiceImpl(AdminReportDao adminReportDao) {
        this.adminReportDao = adminReportDao;
    }

    @Override
    public List<AdminReportListDto> getReportList() {
        return adminReportDao.selectReportList();
    }

    @Override
    public AdminReportDetailDto getReportDetail(Long reportId) {
        return adminReportDao.selectReportDetail(reportId);
    }

    @Override
    @Transactional
    public void acceptReport(Long reportId, Long reviewId) {
        adminReportDao.updateReportAccept(reportId);
        adminReportDao.updateReviewHidden(reviewId);
    }

    @Override
    public void rejectReport(Long reportId) {
        adminReportDao.updateReportReject(reportId);
    }
}