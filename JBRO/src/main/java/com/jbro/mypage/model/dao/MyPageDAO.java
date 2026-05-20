package com.jbro.mypage.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jbro.mypage.model.vo.MemberVo;
import com.jbro.mypage.model.vo.MyPageFavoriteVo;
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
}
