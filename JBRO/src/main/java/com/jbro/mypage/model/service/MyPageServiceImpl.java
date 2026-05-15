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

import com.jbro.auth.model.service.LoginMemberProvider;
import com.jbro.mypage.model.dao.MyPageDAO;
import com.jbro.mypage.model.vo.MemberVo;
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
	private final LoginMemberProvider loginMemberProvider;
	private final String serverPort;

	public MyPageServiceImpl(
		MyPageDAO myPageDAO,
		LoginMemberProvider loginMemberProvider,
		@Value("${server.port:8081}") String serverPort
	) {
		this.myPageDAO = myPageDAO;
		this.loginMemberProvider = loginMemberProvider;
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
		Long memberId = loginMemberProvider.getLoginMemberId();
		return myPageDAO.selectMyPageProfile(memberId);
	}

	@Override
	public boolean isNicknameAvailable(String nickname) {
		Long memberId = loginMemberProvider.getLoginMemberId();
		return myPageDAO.countByNicknameExceptId(nickname, memberId) == 0;
	}

	@Override
	public MemberVo modifyMyPageNickname(String nickname) {
		Long memberId = loginMemberProvider.getLoginMemberId();
		myPageDAO.updateProfileNickname(memberId, nickname);
		return myPageDAO.selectMyPageProfile(memberId);
	}

	@Override
	public ProfileUpdateResponse updateProfileImage(MultipartFile profileImage, HttpSession session) {
		Long memberId = loginMemberProvider.getLoginMemberId(session);

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

	@Override
	public boolean withdrawMember(HttpSession session) {
		Long memberId = loginMemberProvider.getLoginMemberId(session);
		return myPageDAO.updateMemberStatus(memberId, "N") > 0;
	}
}
