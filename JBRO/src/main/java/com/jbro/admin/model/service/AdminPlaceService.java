package com.jbro.admin.model.service;

import java.util.List;
import java.util.Map;

import com.jbro.admin.model.dto.AdminPlaceListDto;
import com.jbro.admin.model.dto.AdminPlaceReviewImageDto;

public interface AdminPlaceService {
    List<AdminPlaceListDto> getPlaceList(String keyword, Boolean noImageOnly);
    List<AdminPlaceReviewImageDto> getReviewImages(Long contentId);
    void registerPlaceImage(Long contentId, String imageUrl, String imgName);
    void deletePlaceImage(Long imageId);
    List<Map<String, Object>> getPlaceImages(Long contentId);
}