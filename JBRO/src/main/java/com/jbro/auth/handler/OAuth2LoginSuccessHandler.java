package com.jbro.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jbro.auth.model.service.JwtTokenProvider;
import com.jbro.auth.model.service.OAuthLoginService;
import com.jbro.auth.model.service.OAuthUserInfoFactory;
import com.jbro.auth.model.vo.OAuthUserInfo;
import com.jbro.mypage.model.vo.MemberVo;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final OAuthUserInfoFactory userInfoFactory;
	private final OAuthLoginService oauthLoginService;
	private final JwtTokenProvider jwtTokenProvider;
	private final String frontendCallbackUrl;

	public OAuth2LoginSuccessHandler(
		OAuthUserInfoFactory userInfoFactory,
		OAuthLoginService oauthLoginService,
		JwtTokenProvider jwtTokenProvider,
		@Value("${app.frontend.oauth-callback-url:http://localhost:3000/oauth2/callback}") String frontendCallbackUrl
	) {
		this.userInfoFactory = userInfoFactory;
		this.oauthLoginService = oauthLoginService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.frontendCallbackUrl = frontendCallbackUrl;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oauth2User = oauthToken.getPrincipal();
		String provider = oauthToken.getAuthorizedClientRegistrationId();

		OAuthUserInfo userInfo = userInfoFactory.create(provider, oauth2User);
		MemberVo member = oauthLoginService.findOrCreateMember(userInfo);
		String accessToken = jwtTokenProvider.createAccessToken(member);

		HttpSession session = request.getSession();
		session.setAttribute("memberId", member.getId());

		String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
		response.sendRedirect(frontendCallbackUrl + "?token=" + encodedToken);
	}
}
