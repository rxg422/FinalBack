package com.jbro.tourDetail.model.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface TravelDetailService {
    Map<String, Object> getDetail(long contentId, Long userId);
    Map<String, Object> getReviews(long contentId, int page, int size);
    void createReview(long contentId, long userId, String content, MultipartFile image);
    void updateReview(long reviewId, long userId, String content, MultipartFile image);
    void deleteReview(long reviewId, long userId);
    void toggleFavorite(long contentId, long userId);
    void reportReview(long reviewId, long userId, String reportType, String reason);
}