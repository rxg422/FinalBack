package com.jbro.admin.model.service;

import com.jbro.admin.model.dto.AdminReviewDetailDto;
import com.jbro.admin.model.dto.AdminReviewListDto;
import java.util.List;

public interface AdminReviewService {
    List<AdminReviewListDto> getReviewList();
    AdminReviewDetailDto getReviewDetail(Long reviewId);
    void hideReview(Long reviewId);
    void showReview(Long reviewId);
}