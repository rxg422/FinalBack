package com.jbro.courseDetail.model.service;

import com.jbro.courseDetail.model.dao.CourseDetailDao;
import com.jbro.courseDetail.model.dto.CourseDetailResponseDto;
import com.jbro.courseDetail.model.dto.CourseDetailResponseDto.*;
import com.jbro.courseDetail.model.vo.CourseDetailVo;
import com.jbro.courseDetail.model.vo.CourseDetailTipVo;
import com.jbro.courseDetail.model.vo.CoursePopularPlaceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseDetailServiceImpl implements CourseDetailService {

    private final CourseDetailDao courseDetailDao;

    @Override
    public CourseDetailResponseDto getCourseDetail(long courseId) {
        List<CourseDetailVo> rows = courseDetailDao.getCourseDetail(courseId);
        if (rows == null || rows.isEmpty()) return null;

        CourseDetailVo first = rows.get(0);

        CourseDetailResponseDto dto = new CourseDetailResponseDto();
        dto.setCourseId(first.getCourseId());
        dto.setTitle(first.getTitle());
        dto.setSubtitle(first.getSubtitle());
        dto.setDuration(first.getDuration());
        dto.setRegion(first.getRegion());
        dto.setTheme(first.getTheme());
        dto.setTotalDistance(first.getTotalDistance());
        dto.setHistoryYear(first.getHistoryYear());
        dto.setHistorySummary(first.getHistorySummary());
        dto.setIntroTitle(first.getIntroTitle());
        dto.setIntroText(first.getIntroText());
        dto.setMultiDay("Y".equals(first.getIsMultiDay()));
        dto.setHeroImage(first.getHeroImage());

        // DAY → PLACE 그룹핑
        Map<Integer, CourseDayDto> dayMap = new LinkedHashMap<>();
        for (CourseDetailVo row : rows) {
            dayMap.computeIfAbsent(row.getDayNo(), dayNo -> {
                CourseDayDto day = new CourseDayDto();
                day.setDayNo(dayNo);
                day.setDayTitle(row.getDayTitle());
                day.setPlaces(new ArrayList<>());
                return day;
            });

            CoursePlaceDto place = new CoursePlaceDto();
            place.setPlaceOrder(row.getPlaceOrder());
            place.setPlaceDesc(row.getPlaceDesc());
            place.setContentId(row.getContentId());
            place.setTitle(row.getPlaceName());
            place.setAddress(row.getAddr1());
            place.setTel(row.getTel());
            place.setMapX(row.getMapX());
            place.setMapY(row.getMapY());
            place.setFirstImage(row.getFirstImage());

            dayMap.get(row.getDayNo()).getPlaces().add(place);
        }
        dto.setDays(new ArrayList<>(dayMap.values()));

        // 꿀팁
        List<CourseDetailTipVo> tipVos = courseDetailDao.getCourseTips(courseId);
        List<String> tips = new ArrayList<>();
        for (CourseDetailTipVo tip : tipVos) {
            tips.add(tip.getTipContent());
        }
        dto.setTips(tips);

        // 인기 여행지 (카테고리별 찜 많은 순 1개씩)
        List<CoursePopularPlaceVo> popularVos = courseDetailDao.getPopularPlaces(first.getRegionCode());
        List<PopularPlaceDto> popularPlaces = new ArrayList<>();
        for (CoursePopularPlaceVo vo : popularVos) {
            PopularPlaceDto p = new PopularPlaceDto();
            p.setContentId(vo.getContentId());
            p.setTitle(vo.getTitle());
            p.setFirstImage(vo.getFirstImage());
            p.setCategoryName(vo.getCategoryName());
            popularPlaces.add(p);
        }
        dto.setPopularPlaces(popularPlaces);

        return dto;
    }
}