package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminReviewDetailDto;
import com.jbro.admin.model.dto.AdminReviewListDto;
import java.util.List;

public interface AdminReviewDao {
    List<AdminReviewListDto> selectReviewList();
    AdminReviewDetailDto selectReviewDetail(Long reviewId);
    int updateReviewHide(Long reviewId);
    int updateReviewShow(Long reviewId);
}