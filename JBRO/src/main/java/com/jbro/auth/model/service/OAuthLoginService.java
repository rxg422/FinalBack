package com.jbro.auth.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbro.auth.model.dao.AuthDAO;
import com.jbro.auth.model.vo.OAuthUserInfo;
import com.jbro.mypage.model.vo.MemberVo;

@Service
public class OAuthLoginService {

	private final AuthDAO authDAO;

	public OAuthLoginService(AuthDAO authDAO) {
		this.authDAO = authDAO;
	}

	@Transactional
	public MemberVo findOrCreateMember(OAuthUserInfo userInfo) {
		MemberVo member = authDAO.selectMemberBySocialAccount(userInfo.getProvider(), userInfo.getProviderId());

		if (member != null) {
			return member;
		}

		if (userInfo.getEmail() != null) {
			member = authDAO.selectActiveMemberByEmail(userInfo.getEmail());
			if (member != null) {
				authDAO.insertSocialAccount(
					member.getId(),
					userInfo.getProvider(),
					userInfo.getProviderId(),
					userInfo.getEmail(),
					userInfo.getProfile()
				);
				return member;
			}
		}

		MemberVo newMember = new MemberVo();
		newMember.setEmail(userInfo.getEmail());
		newMember.setNickname(userInfo.getNickname());
		newMember.setProfile(userInfo.getProfile());
		newMember.setStatus("Y");

		authDAO.insertMember(newMember);
		authDAO.insertSocialAccount(
			newMember.getId(),
			userInfo.getProvider(),
			userInfo.getProviderId(),
			userInfo.getEmail(),
			userInfo.getProfile()
		);

		return newMember;
	}
}
