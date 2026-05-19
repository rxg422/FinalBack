package com.jbro.courseList.model.service;

import com.jbro.courseList.model.dao.CourseListDao;
import com.jbro.courseList.model.dto.CourseListResponseDto;
import com.jbro.courseList.model.vo.CourseListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseListServiceImpl implements CourseListService {

    private final CourseListDao courseListDao;

    @Override
    public List<CourseListResponseDto> getCourseList() {
        List<CourseListVo> voList = courseListDao.selectCourseList();

        return voList.stream().map(vo -> {
            CourseListResponseDto dto = new CourseListResponseDto();
            dto.setCourseId(vo.getCourseId());
            dto.setTitle(vo.getTitle());
            dto.setSubtitle(vo.getSubtitle());
            dto.setDuration(vo.getDuration());
            dto.setTheme(vo.getTheme());
            dto.setHeroImage(vo.getHeroImage());
            dto.setRegionName(vo.getRegionName());
            dto.setPlaceTitles(courseListDao.selectPlaceTitles(vo.getCourseId()));
            return dto;
        }).collect(Collectors.toList());
    }
}