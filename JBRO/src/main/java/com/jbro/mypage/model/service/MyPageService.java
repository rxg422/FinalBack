package com.jbro.mypage.model.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.ProfileUpdateResponse;

public interface MyPageService {

	List<MemberVo> getMemberList();

	MemberVo getMember(Long memberId);

	int addMember(MemberVo member);

	int modifyMember(MemberVo member);

	int removeMember(Long memberId);

	MemberVo getMyPageProfile();

	boolean isNicknameAvailable(String nickname);

	MemberVo modifyMyPageNickname(String nickname);

	ProfileUpdateResponse updateProfileImage(MultipartFile profileImage, HttpSession session);

	boolean withdrawMember(HttpSession session);
}
