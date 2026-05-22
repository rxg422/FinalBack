package com.jbro.admin.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.admin.model.dao.AdminCourseDao;
import com.jbro.admin.model.dto.AdminCourseDayDto;
import com.jbro.admin.model.dto.AdminCourseDetailDto;
import com.jbro.admin.model.dto.AdminCourseListDto;
import com.jbro.admin.model.dto.AdminCoursePlaceDto;
import com.jbro.admin.model.dto.AdminCoursePlaceSearchDto;
import com.jbro.admin.model.dto.AdminCourseSaveDto;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    private final AdminCourseDao adminCourseDao;
    
    @Value("${server.port}")
    private String serverPort;

    public AdminCourseServiceImpl(AdminCourseDao adminCourseDao) {
        this.adminCourseDao = adminCourseDao;
    }

    @Override
    public List<AdminCourseListDto> getCourseList() {
        return adminCourseDao.selectCourseList();
    }

    @Override
    public AdminCourseDetailDto getCourseDetail(Long courseId) {
        AdminCourseDetailDto detail = adminCourseDao.selectCourseDetail(courseId);
        if (detail == null) return null;

        List<AdminCourseDayDto> days = adminCourseDao.selectCourseDays(courseId);
        days.forEach(day -> {
            List<AdminCoursePlaceDto> places = adminCourseDao.selectCoursePlaces(day.getCourseDayId());
            day.setPlaces(places);
        });
        detail.setDays(days);
        detail.setTips(adminCourseDao.selectCourseTips(courseId));
        return detail;
    }

    @Override
    public List<AdminCoursePlaceSearchDto> searchPlaces(String keyword, Integer categoryId) {
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("categoryId", categoryId);
        return adminCourseDao.searchPlaces(param);
    }

    @Override
    @Transactional
    public Long registerCourse(AdminCourseSaveDto dto) {
        adminCourseDao.insertCourse(dto);
        Long courseId = dto.getCourseId(); // useGeneratedKeys로 받아옴
        saveDaysAndTips(courseId, dto);
        return courseId;
    }

    @Override
    @Transactional
    public void updateCourse(Long courseId, AdminCourseSaveDto dto) {
        // 기존 데이터 삭제
        adminCourseDao.deleteCourseTips(courseId);
        adminCourseDao.deleteCoursePlaces(courseId);
        adminCourseDao.deleteCourseDays(courseId);

        // 기본 정보 수정
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("title", dto.getTitle());
        param.put("subtitle", dto.getSubtitle());
        param.put("duration", dto.getDuration());
        param.put("region", dto.getRegion());
        param.put("theme", dto.getTheme());
        param.put("totalDistance", dto.getTotalDistance());
        param.put("historyYear", dto.getHistoryYear());
        param.put("historySummary", dto.getHistorySummary());
        param.put("introTitle", dto.getIntroTitle());
        param.put("introText", dto.getIntroText());
        param.put("heroImage", dto.getHeroImage());
        param.put("activeYn", dto.getActiveYn());
        adminCourseDao.updateCourse(param);

        // DAY, 장소, 팁 새로 저장
        saveDaysAndTips(courseId, dto);
    }

    private void saveDaysAndTips(Long courseId, AdminCourseSaveDto dto) {
        if (dto.getDays() != null) {
            dto.getDays().forEach(day -> {
                Map<String, Object> dayParam = new HashMap<>();
                dayParam.put("courseId", courseId);
                dayParam.put("dayNo", day.getDayNo());
                dayParam.put("dayTitle", day.getDayTitle());
                adminCourseDao.insertCourseDay(dayParam);

                // BigInteger → Long 변환
                Object generatedKey = dayParam.get("courseDayId");
                Long courseDayId = ((Number) generatedKey).longValue();

                if (day.getPlaces() != null) {
                    day.getPlaces().forEach(place -> {
                        Map<String, Object> placeParam = new HashMap<>();
                        placeParam.put("courseDayId", courseDayId);
                        placeParam.put("placeId", place.getPlaceId());
                        placeParam.put("placeOrder", place.getPlaceOrder());
                        placeParam.put("placeDesc", place.getPlaceDesc());
                        adminCourseDao.insertCoursePlace(placeParam);
                    });
                }
            });
        }

        if (dto.getTips() != null) {
            for (int i = 0; i < dto.getTips().size(); i++) {
                Map<String, Object> tipParam = new HashMap<>();
                tipParam.put("courseId", courseId);
                tipParam.put("tipContent", dto.getTips().get(i));
                tipParam.put("tipOrder", i + 1);
                adminCourseDao.insertCourseTip(tipParam);
            }
        }
    }

    @Override
    public void activateCourse(Long courseId) {
        adminCourseDao.updateCourseActive(courseId);
    }

    @Override
    public void inactivateCourse(Long courseId) {
        adminCourseDao.updateCourseInactive(courseId);
    }
    @Override
    public List<Map<String, Object>> getLdongList() {
        return adminCourseDao.selectLdongList();
    }

@Override
public String uploadCourseImage(MultipartFile image) {
    if (image == null || image.isEmpty()) {
        throw new IllegalArgumentException("업로드할 이미지를 선택해주세요.");
    }

    String contentType = image.getContentType();
    if (contentType == null || !Set.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
        throw new IllegalArgumentException("jpg, png, webp 이미지만 업로드할 수 있습니다.");
    }

    Map<String, String> extensions = Map.of(
        "image/jpeg", ".jpg",
        "image/png", ".png",
        "image/webp", ".webp"
    );

    String extension = extensions.get(contentType);
    String fileName = "course-" + System.currentTimeMillis() + extension;
    Path uploadDir = Paths.get("uploads", "courses").toAbsolutePath().normalize();
    Path targetPath = uploadDir.resolve(fileName).normalize();

    try {
        Files.createDirectories(uploadDir);
        image.transferTo(targetPath);
    } catch (IOException e) {
        throw new IllegalStateException("코스 이미지 저장에 실패했습니다.", e);
    }

    return "http://localhost:" + serverPort + "/uploads/courses/" + fileName;
}
}