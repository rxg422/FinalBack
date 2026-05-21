package com.jbro.auth.model.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.jbro.auth.model.dao.AuthDAO;
import com.jbro.auth.model.vo.OAuthUserInfo;
import com.jbro.mypage.model.vo.MemberVo;

@Service
public class OAuthLoginService {

	private final AuthDAO authDAO;
	private final SqlSessionFactory sqlSessionFactory;

	public OAuthLoginService(AuthDAO authDAO, SqlSessionFactory sqlSessionFactory) {
		this.authDAO = authDAO;
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public MemberVo findOrCreateMember(OAuthUserInfo userInfo) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			// 에러 핸들링: 필수 정보 검증
			if (userInfo == null) {
				throw new IllegalArgumentException("사용자 정보가 없습니다!");
			}
			if (userInfo.getProviderId() == null || userInfo.getProviderId().isEmpty()) {
				throw new IllegalArgumentException("소셜 로그인 ID가 없습니다!");
			}
			if (userInfo.getProvider() == null || userInfo.getProvider().isEmpty()) {
				throw new IllegalArgumentException("소셜 로그인 제공자가 없습니다!");
			}

			AuthDAO mapper = session.getMapper(AuthDAO.class);

			// 1단계: 소셜 계정이 이미 있는지 확인
			MemberVo member = mapper.selectMemberBySocialAccount(userInfo.getProvider(), userInfo.getProviderId());
			if (member != null) {
				return member;
			}

			// 2단계: 이메일로 기존 계정이 있는지 확인
			if (userInfo.getEmail() != null && !userInfo.getEmail().isEmpty()) {
				member = mapper.selectActiveMemberByEmail(userInfo.getEmail());
				if (member != null) {
					int insertResult = mapper.insertSocialAccount(
						member.getId(),
						userInfo.getProvider(),
						userInfo.getProviderId(),
						userInfo.getEmail(),
						userInfo.getProfile()
					);
					if (insertResult <= 0) {
						throw new RuntimeException("소셜 계정 연동에 실패했습니다!");
					}
					session.commit();
					return member;
				}
			}

			// 3단계: 새로운 사용자 생성
			MemberVo newMember = new MemberVo();
			newMember.setEmail(userInfo.getEmail());
			newMember.setNickname(null);  // 닉네임셋업 페이지에서 설정하도록 null로 설정
			newMember.setProfile(userInfo.getProfile());
			newMember.setStatus("Y");
			newMember.setRole("USER");  // ✅ 신규 사용자는 기본적으로 USER 권한
			newMember.setCreatedAt(java.time.LocalDateTime.now());

			int memberInsertResult = mapper.insertMember(newMember);
			if (memberInsertResult <= 0) {
				throw new RuntimeException("사용자 생성에 실패했습니다!");
			}

			if (newMember.getId() == null || newMember.getId() <= 0) {
				throw new RuntimeException("생성된 사용자 ID가 없습니다!");
			}

			int socialAccountResult = mapper.insertSocialAccount(
				newMember.getId(),
				userInfo.getProvider(),
				userInfo.getProviderId(),
				userInfo.getEmail(),
				userInfo.getProfile()
			);

			if (socialAccountResult <= 0) {
				throw new RuntimeException("소셜 계정 정보 저장에 실패했습니다!");
			}

			session.commit();
			session.clearCache();  // ⭐ 캐시 지우기

			// ✅ INSERT 성공 후 newMember 직접 반환
			// SELECT를 건너뛰고 이미 INSERT된 데이터 사용
			return newMember;
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}