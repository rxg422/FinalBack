package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminPlaceListDto;
import com.jbro.admin.model.dto.AdminPlaceReviewImageDto;
import java.util.List;
import java.util.Map;

public interface AdminPlaceDao {
    List<AdminPlaceListDto> selectPlaceList(Map<String, Object> param);
    List<AdminPlaceReviewImageDto> selectReviewImages(Long contentId);
    int insertPlaceImage(Map<String, Object> param);
    int deletePlaceImage(Long imageId);
    List<Map<String, Object>> selectPlaceImages(Long contentId);
    int updatePlaceFirstImage(Long contentId, String imageUrl);
    int updatePlaceFirstImageIfEmpty(Long contentId, String imageUrl);
    Map<String, Object> selectPlaceImageById(Long imageId);
    int clearFirstImageIfMatch(Long contentId, String imageUrl);
}