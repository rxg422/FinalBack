package com.jbro.admin.model.service;

import com.jbro.admin.model.dao.AdminPlaceDao;
import com.jbro.admin.model.dto.AdminPlaceListDto;
import com.jbro.admin.model.dto.AdminPlaceReviewImageDto;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminPlaceServiceImpl implements AdminPlaceService {

    private final AdminPlaceDao adminPlaceDao;

    public AdminPlaceServiceImpl(AdminPlaceDao adminPlaceDao) {
        this.adminPlaceDao = adminPlaceDao;
    }

    @Override
    public List<AdminPlaceListDto> getPlaceList(String keyword, Boolean noImageOnly) {
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("noImageOnly", noImageOnly);
        return adminPlaceDao.selectPlaceList(param);
    }

    @Override
    public List<AdminPlaceReviewImageDto> getReviewImages(Long contentId) {
        return adminPlaceDao.selectReviewImages(contentId);
    }

    @Override
    public void registerPlaceImage(Long contentId, String imageUrl, String imgName) {
        Map<String, Object> param = new HashMap<>();
        param.put("contentId", contentId);
        param.put("originImgUrl", imageUrl);
        param.put("smallImageUrl", imageUrl);
        param.put("imgName", imgName);
        param.put("serialNum", "1");
        adminPlaceDao.insertPlaceImage(param);
        
        // TOUR_PLACE.FIRST_IMAGE도 업데이트
        adminPlaceDao.updatePlaceFirstImage(contentId, imageUrl);
    }

    @Override
    public void deletePlaceImage(Long imageId) {
        adminPlaceDao.deletePlaceImage(imageId);
    }
    @Override
    public List<Map<String, Object>> getPlaceImages(Long contentId) {
        return adminPlaceDao.selectPlaceImages(contentId);
    }
}