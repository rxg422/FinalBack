package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminReviewDao;
import com.jbro.admin.model.dto.AdminReviewDetailDto;
import com.jbro.admin.model.dto.AdminReviewListDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    private final AdminReviewDao adminReviewDao;

    public AdminReviewServiceImpl(AdminReviewDao adminReviewDao) {
        this.adminReviewDao = adminReviewDao;
    }

    @Override
    public List<AdminReviewListDto> getReviewList() {
        return adminReviewDao.selectReviewList();
    }

    @Override
    public AdminReviewDetailDto getReviewDetail(Long reviewId) {
        return adminReviewDao.selectReviewDetail(reviewId);
    }

    @Override
    public void hideReview(Long reviewId) {
        adminReviewDao.updateReviewHide(reviewId);
    }

    @Override
    public void showReview(Long reviewId) {
        adminReviewDao.updateReviewShow(reviewId);
    }
}