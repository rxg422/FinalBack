package com.jbro.mypage.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.MyPageFavoriteVo;
import com.jbro.mypage.model.vo.MyPagePlannerCandidateVo;
import com.jbro.mypage.model.vo.MyPagePlannerDayVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailDayVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailPlaceVo;
import com.jbro.mypage.model.vo.MyPagePlannerDetailVo;
import com.jbro.mypage.model.vo.MyPagePlannerVo;
import com.jbro.mypage.model.vo.MyPageReportVo;
import com.jbro.mypage.model.vo.MyPageReviewVo;

@Mapper
public interface MyPageDAO {

	List<MemberVo> selectMemberList();

	MemberVo selectMember(Long memberId);

	int insertMember(MemberVo member);

	int updateMember(MemberVo member);

	int deleteMember(Long memberId);

	MemberVo selectMyPageProfile(Long memberId);

	int countByNicknameExceptId(@Param("nickname") String nickname, @Param("memberId") Long memberId);

	int updateProfileNickname(@Param("memberId") Long memberId, @Param("nickname") String nickname);

	int updateProfileImage(@Param("memberId") Long memberId, @Param("profile") String profile);

	int updateMemberStatus(@Param("memberId") Long memberId, @Param("status") String status);

	List<MyPageFavoriteVo> selectMyFavorites(Long memberId);

	List<MyPageReviewVo> selectMyReviews(Long memberId);

	List<MyPageReportVo> selectMyReports(Long memberId);

	List<MyPagePlannerCandidateVo> selectPlannerCandidates(
		@Param("source") String source,
		@Param("category") String category,
		@Param("keyword") String keyword,
		@Param("regions") List<String> regions,
		@Param("excludeContentIds") List<Long> excludeContentIds,
		@Param("limit") int limit
	);

	List<MyPagePlannerVo> selectMyPlanners(Long memberId);

	MyPagePlannerVo selectMyPlanner(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	MyPagePlannerDetailVo selectMyPlannerDetail(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	List<String> selectMyPlannerRegions(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	List<String> selectMyPlannerThemas(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	List<MyPagePlannerDetailDayVo> selectMyPlannerDays(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	List<MyPagePlannerDetailPlaceVo> selectMyPlannerPlaces(@Param("dayId") Long dayId);

	int insertPlanner(MyPagePlannerVo planner);

	int updatePlanner(@Param("planner") MyPagePlannerVo planner, @Param("memberId") Long memberId);

	int deletePlanner(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	int deletePlannerPlaces(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	int deletePlannerDays(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	int deletePlannerRegions(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	int deletePlannerThemas(@Param("plannerId") Long plannerId, @Param("memberId") Long memberId);

	int insertPlannerDay(MyPagePlannerDayVo day);

	Integer selectSignguCdByRegion(String region);

	int insertPlannerRegion(@Param("plannerId") Long plannerId, @Param("signguCd") Integer signguCd);

	int insertPlannerThema(@Param("plannerId") Long plannerId, @Param("thema") String thema);

	int insertPlannerPlace(
		@Param("dayId") Long dayId,
		@Param("visitOrder") int visitOrder,
		@Param("contentId") Long contentId,
		@Param("description") String description
	);
}
