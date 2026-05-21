package com.jbro.mypage.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.MyPageFavoriteVo;
import com.jbro.mypage.model.vo.MyPagePlannerCandidateVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailVo;
import com.jbro.mypage.model.vo.MyPagePlannerRequestVo;
import com.jbro.mypage.model.vo.MyPagePlannerVo;
import com.jbro.mypage.model.vo.MyPageReportVo;
import com.jbro.mypage.model.vo.MyPageReviewVo;
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

	ProfileUpdateResponse updateProfileImage(MultipartFile profileImage);

	boolean withdrawMember();

	List<MyPageFavoriteVo> getMyFavorites();

	List<MyPageReviewVo> getMyReviews();

	List<MyPageReportVo> getMyReports();

	List<MyPagePlannerCandidateVo> getPlannerCandidates(
		String source,
		String category,
		String keyword,
		String regions,
		String excludeContentIds
	);

	List<MyPagePlannerVo> getMyPlanners();

	MyPagePlannerDetailVo getMyPlannerDetail(Long plannerId);

	MyPagePlannerVo createMyPlanner(MyPagePlannerRequestVo request);

	MyPagePlannerVo updateMyPlanner(Long plannerId, MyPagePlannerRequestVo request);
}
