package com.jbro.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jbro.mypage.model.vo.MemberVo;

@Mapper
public interface AuthDAO {

	MemberVo selectMemberBySocialAccount(@Param("provider") String provider, @Param("providerId") String providerId);

	MemberVo selectActiveMemberByEmail(@Param("email") String email);
	
	 // ========== ID로 사용자 정보 조회 ==========
    MemberVo selectMemberById(@Param("id") Long id);

	int insertMember(MemberVo member);

	int insertSocialAccount(
		@Param("userId") Long userId,
		@Param("provider") String provider,
		@Param("providerId") String providerId,
		@Param("email") String email,
		@Param("profile") String profile
	);
}
