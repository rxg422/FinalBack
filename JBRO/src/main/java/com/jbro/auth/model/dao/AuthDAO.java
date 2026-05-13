package com.jbro.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jbro.mypage.model.vo.MemberVo;

@Mapper
public interface AuthDAO {

	MemberVo selectMemberBySocialAccount(@Param("provider") String provider, @Param("providerId") String providerId);

	MemberVo selectActiveMemberByEmail(@Param("email") String email);

	int insertMember(MemberVo member);

	int insertSocialAccount(
		@Param("userId") Long userId,
		@Param("provider") String provider,
		@Param("providerId") String providerId,
		@Param("email") String email,
		@Param("profile") String profile
	);
}
