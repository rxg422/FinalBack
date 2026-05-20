package com.jbro.mypage.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.jbro.mypage.model.vo.MyPageReportVo;
import com.jbro.mypage.model.vo.MyPageReviewVo;
import com.jbro.mypage.model.vo.ProfileUpdateResponse;

@Service
public class MyPageServiceImpl implements MyPageService {

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
