package com.jbro.mypage.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jbro.mypage.model.dao.MyPageDAO;
import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.ProfileUpdateResponse;

@Service
public class MyPageServiceImpl implements MyPageService {

	private static final Long TEMP_LOGIN_MEMBER_ID = 1L;
	private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
	private static final Map<String, String> EXTENSIONS_BY_CONTENT_TYPE = Map.of(
		"image/jpeg", ".jpg",
		"image/png", ".png",
		"image/webp", ".webp"
	);

	private final MyPageDAO myPageDAO;
	private final String serverPort;

	public MyPageServiceImpl(MyPageDAO myPageDAO, @Value("${server.port:8080}") String serverPort) {
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
		return myPageDAO.selectMyPageProfile(TEMP_LOGIN_MEMBER_ID);
	}

	@Override
	public boolean isNicknameAvailable(String nickname) {
		return myPageDAO.countByNicknameExceptId(nickname, TEMP_LOGIN_MEMBER_ID) == 0;
	}

	@Override
	public MemberVo modifyMyPageNickname(String nickname) {
		myPageDAO.updateProfileNickname(TEMP_LOGIN_MEMBER_ID, nickname);
		return myPageDAO.selectMyPageProfile(TEMP_LOGIN_MEMBER_ID);
	}

	@Override
	public ProfileUpdateResponse updateProfileImage(MultipartFile profileImage, HttpSession session) {
		Long memberId = getLoginMemberId(session);

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
		myPageDAO.updateProfileImage(memberId, profileUrl);

		MemberVo profile = myPageDAO.selectMyPageProfile(memberId);
		return new ProfileUpdateResponse(true, "프로필 이미지가 수정되었습니다.", profile);
	}

	private Long getLoginMemberId(HttpSession session) {
		Object sessionMemberId = session.getAttribute("memberId");

		if (sessionMemberId instanceof Long memberId) {
			return memberId;
		}

		if (sessionMemberId instanceof Integer memberId) {
			return memberId.longValue();
		}

		if (sessionMemberId instanceof String memberId) {
			return Long.parseLong(memberId);
		}

		return TEMP_LOGIN_MEMBER_ID;
	}
}

