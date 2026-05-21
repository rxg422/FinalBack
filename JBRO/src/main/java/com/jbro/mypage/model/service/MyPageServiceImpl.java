package com.jbro.mypage.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.mypage.model.dao.MyPageDAO;
import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.MyPageFavoriteVo;
import com.jbro.mypage.model.vo.MyPagePlannerCandidateVo;
import com.jbro.mypage.model.vo.MyPagePlannerDayRequestVo;
import com.jbro.mypage.model.vo.MyPagePlannerDayVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailDayVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailVo;
import com.jbro.mypage.model.vo.MyPagePlannerPlaceRequestVo;
import com.jbro.mypage.model.vo.MyPagePlannerRequestVo;
import com.jbro.mypage.model.vo.MyPagePlannerVo;
import com.jbro.mypage.model.vo.MyPageReportVo;
import com.jbro.mypage.model.vo.MyPageReviewVo;
import com.jbro.mypage.model.vo.ProfileUpdateResponse;

@Service
public class MyPageServiceImpl implements MyPageService {

	private static final int PLANNER_CANDIDATE_LIMIT = 20;
	private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
	private static final Map<String, String> EXTENSIONS_BY_CONTENT_TYPE = Map.of(
		"image/jpeg", ".jpg",
		"image/png", ".png",
		"image/webp", ".webp"
	);

	private final MyPageDAO myPageDAO;
	private final String serverPort;

	public MyPageServiceImpl(
		MyPageDAO myPageDAO,
		@Value("${server.port:8081}") String serverPort
	) {
		this.myPageDAO = myPageDAO;
		this.serverPort = serverPort;
	}

	@Override
	public List<MemberVo> getMemberList() {
		return myPageDAO.selectMemberList();
	}

	@Override
	public MemberVo getMember(Long memberId) {
		return myPageDAO.selectMember(memberId);
	}

	@Override
	public int addMember(MemberVo member) {
		return myPageDAO.insertMember(member);
	}

	@Override
	public int modifyMember(MemberVo member) {
		return myPageDAO.updateMember(member);
	}

	@Override
	public int removeMember(Long memberId) {
		return myPageDAO.deleteMember(memberId);
	}

	@Override
	public MemberVo getMyPageProfile() {
		Long memberId = getRequiredLoginMemberId();
		MemberVo profile = myPageDAO.selectMyPageProfile(memberId);

		if (profile == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "활성 회원 정보를 찾을 수 없습니다.");
		}

		return profile;
	}

	@Override
	public boolean isNicknameAvailable(String nickname) {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.countByNicknameExceptId(nickname, memberId) == 0;
	}

	@Override
	@Transactional
	public MemberVo modifyMyPageNickname(String nickname) {
		Long memberId = getRequiredLoginMemberId();
		int updateCount = myPageDAO.updateProfileNickname(memberId, nickname);

		if (updateCount == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "닉네임을 수정할 활성 회원 정보를 찾을 수 없습니다.");
		}

		return getMyPageProfile();
	}

	@Override
	@Transactional
	public ProfileUpdateResponse updateProfileImage(MultipartFile profileImage) {
		Long memberId = getRequiredLoginMemberId();

		if (profileImage == null || profileImage.isEmpty()) {
			return new ProfileUpdateResponse(false, "업로드할 프로필 이미지를 선택해주세요.", null);
		}

		String contentType = profileImage.getContentType();
		if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
			return new ProfileUpdateResponse(false, "jpg, png, webp 이미지만 업로드할 수 있습니다.", null);
		}

		String extension = EXTENSIONS_BY_CONTENT_TYPE.get(contentType);
		String fileName = "member-" + memberId + extension;
		Path uploadDir = Paths.get("uploads", "profile").toAbsolutePath().normalize();
		Path targetPath = uploadDir.resolve(fileName).normalize();

		try {
			Files.createDirectories(uploadDir);
			profileImage.transferTo(targetPath);
		} catch (IOException exception) {
			throw new IllegalStateException("프로필 이미지 저장에 실패했습니다.", exception);
		}

		String profileUrl = "http://localhost:" + serverPort + "/uploads/profile/" + fileName
			+ "?v=" + System.currentTimeMillis();
		int updateCount = myPageDAO.updateProfileImage(memberId, profileUrl);

		if (updateCount == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로필 이미지를 수정할 활성 회원 정보를 찾을 수 없습니다.");
		}

		MemberVo profile = getMyPageProfile();
		return new ProfileUpdateResponse(true, "프로필 이미지가 수정되었습니다.", profile);
	}

	@Override
	@Transactional
	public boolean withdrawMember() {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.updateMemberStatus(memberId, "N") > 0;
	}

	@Override
	public List<MyPageFavoriteVo> getMyFavorites() {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.selectMyFavorites(memberId);
	}

	@Override
	public List<MyPageReviewVo> getMyReviews() {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.selectMyReviews(memberId);
	}

	@Override
	public List<MyPageReportVo> getMyReports() {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.selectMyReports(memberId);
	}

	@Override
	public List<MyPagePlannerCandidateVo> getPlannerCandidates(
		String source,
		String category,
		String keyword,
		String regions,
		String excludeContentIds
	) {
		getRequiredLoginMemberId();

		String normalizedSource = source == null ? "search" : source.trim().toLowerCase();
		if (!Set.of("search", "ai").contains(normalizedSource)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 장소 후보 조회 방식입니다.");
		}

		String normalizedCategory = null;
		if (category != null && !category.isBlank()) {
			normalizedCategory = category.trim();
		}

		if (normalizedCategory != null && !Set.of("관광지", "숙소", "식당", "행사").contains(normalizedCategory)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 카테고리입니다.");
		}

		String normalizedKeyword = null;
		if (keyword != null && !keyword.isBlank()) {
			normalizedKeyword = keyword.trim();
		}

		List<String> regionList = parseCsv(regions);
		List<Long> excludeIdList = parseLongCsv(excludeContentIds);

		return myPageDAO.selectPlannerCandidates(
			normalizedSource,
			normalizedCategory,
			normalizedKeyword,
			regionList,
			excludeIdList,
			PLANNER_CANDIDATE_LIMIT
		);
	}

	@Override
	public List<MyPagePlannerVo> getMyPlanners() {
		Long memberId = getRequiredLoginMemberId();
		return myPageDAO.selectMyPlanners(memberId);
	}

	@Override
	public MyPagePlannerDetailVo getMyPlannerDetail(Long plannerId) {
		Long memberId = getRequiredLoginMemberId();
		MyPagePlannerDetailVo planner = myPageDAO.selectMyPlannerDetail(plannerId, memberId);

		if (planner == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "플래너 정보를 찾을 수 없습니다.");
		}

		planner.setRegions(myPageDAO.selectMyPlannerRegions(plannerId, memberId));

		List<MyPagePlannerDetailDayVo> days = myPageDAO.selectMyPlannerDays(plannerId, memberId);
		for (MyPagePlannerDetailDayVo day : days) {
			day.setPlaces(myPageDAO.selectMyPlannerPlaces(day.getDayId()));
		}
		planner.setDays(days);

		return planner;
	}

	@Override
	@Transactional
	public MyPagePlannerVo createMyPlanner(MyPagePlannerRequestVo request) {
		Long memberId = getRequiredLoginMemberId();
		MyPagePlannerVo planner = buildPlanner(memberId, request);

		myPageDAO.insertPlanner(planner);
		savePlannerRegions(planner.getPlannerId(), request);
		savePlannerDays(planner.getPlannerId(), request);

		return myPageDAO.selectMyPlanner(planner.getPlannerId(), memberId);
	}

	@Override
	@Transactional
	public MyPagePlannerVo updateMyPlanner(Long plannerId, MyPagePlannerRequestVo request) {
		Long memberId = getRequiredLoginMemberId();
		MyPagePlannerVo planner = buildPlanner(memberId, request);
		planner.setPlannerId(plannerId);

		int updateCount = myPageDAO.updatePlanner(planner, memberId);
		if (updateCount == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 활성 플래너를 찾을 수 없습니다.");
		}

		myPageDAO.deletePlannerPlaces(plannerId, memberId);
		myPageDAO.deletePlannerDays(plannerId, memberId);
		myPageDAO.deletePlannerRegions(plannerId, memberId);
		savePlannerRegions(plannerId, request);
		savePlannerDays(plannerId, request);

		return myPageDAO.selectMyPlanner(plannerId, memberId);
	}

	private MyPagePlannerVo buildPlanner(Long memberId, MyPagePlannerRequestVo request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "플래너 요청 정보가 필요합니다.");
		}

		if (request.getTitle() == null || request.getTitle().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "플래너 제목을 입력해주세요.");
		}

		MyPagePlannerVo planner = new MyPagePlannerVo();
		planner.setUserId(memberId);
		planner.setTitle(request.getTitle().trim());
		planner.setDescription(request.getDescription());
		planner.setIsPublic(defaultString(request.getIsPublic(), "N"));
		planner.setStatus(defaultString(request.getStatus(), "Y"));
		return planner;
	}

	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}

	private List<String> parseCsv(String value) {
		List<String> result = new ArrayList<>();
		if (value == null || value.isBlank()) {
			return result;
		}

		for (String item : value.split(",")) {
			String trimmed = item.trim();
			if (!trimmed.isEmpty()) {
				result.add(trimmed);
			}
		}

		return result;
	}

	private List<Long> parseLongCsv(String value) {
		List<Long> result = new ArrayList<>();
		if (value == null || value.isBlank()) {
			return result;
		}

		for (String item : value.split(",")) {
			String trimmed = item.trim();
			if (trimmed.isEmpty()) {
				continue;
			}

			try {
				result.add(Long.parseLong(trimmed));
			} catch (NumberFormatException exception) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제외할 장소 ID 형식이 올바르지 않습니다.");
			}
		}

		return result;
	}

	private void savePlannerRegions(Long plannerId, MyPagePlannerRequestVo request) {
		if (request.getRegions() == null || request.getRegions().isEmpty()) {
			return;
		}

		for (String region : request.getRegions()) {
			if (region == null || region.isBlank()) {
				continue;
			}

			Integer signguCd = myPageDAO.selectSignguCdByRegion(region.trim());
			if (signguCd == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지역을 찾을 수 없습니다: " + region);
			}

			myPageDAO.insertPlannerRegion(plannerId, signguCd);
		}
	}

	private void savePlannerDays(Long plannerId, MyPagePlannerRequestVo request) {
		if (request.getDays() == null || request.getDays().isEmpty()) {
			return;
		}

		for (MyPagePlannerDayRequestVo dayRequest : request.getDays()) {
			MyPagePlannerDayVo day = new MyPagePlannerDayVo();
			day.setPlannerId(plannerId);
			day.setDayNo(dayRequest.getDayNo() == null ? 1 : dayRequest.getDayNo());
			myPageDAO.insertPlannerDay(day);

			savePlannerPlaces(day.getDayId(), dayRequest);
		}
	}

	private void savePlannerPlaces(Long dayId, MyPagePlannerDayRequestVo dayRequest) {
		if (dayRequest.getPlaces() == null || dayRequest.getPlaces().isEmpty()) {
			return;
		}

		int visitOrder = 1;
		for (MyPagePlannerPlaceRequestVo place : dayRequest.getPlaces()) {
			if (place == null || place.getContentId() == null) {
				continue;
			}

			int order = place.getVisitOrder() == null ? visitOrder : place.getVisitOrder();

			myPageDAO.insertPlannerPlace(dayId, order, place.getContentId(), place.getDescription());
			visitOrder++;
		}
	}

	private Long getRequiredLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof Long memberId) {
			return memberId;
		}

		if (principal instanceof Integer memberId) {
			return memberId.longValue();
		}

		if (principal instanceof String memberId && memberId.matches("\\d+")) {
			return Long.parseLong(memberId);
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 회원 정보를 확인할 수 없습니다.");
	}
}
