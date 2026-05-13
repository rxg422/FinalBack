package com.jbro.auth.model.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jbro.mypage.model.vo.MemberVo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final Key secretKey;
	private final long expirationMillis;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.access-token-expiration-millis:3600000}") long expirationMillis
	) {
		this.secretKey = Keys.hmacShaKeyFor(resolveSecretBytes(secret));
		this.expirationMillis = expirationMillis;
	}

	public String createAccessToken(MemberVo member) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusMillis(expirationMillis);
		boolean nicknameSet = member.getNickname() != null && !member.getNickname().isBlank();

		return Jwts.builder()
			.setSubject(String.valueOf(member.getId()))
			.claim("memberId", member.getId())
			.claim("email", member.getEmail())
			.claim("nickname", member.getNickname())
			.claim("profileImg", member.getProfile())
			.claim("nicknameSet", nicknameSet)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(expiresAt))
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public Long getMemberId(String token) {
		Object memberId = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("memberId");

		if (memberId instanceof Number number) {
			return number.longValue();
		}

		return Long.parseLong(String.valueOf(memberId));
	}

	private byte[] resolveSecretBytes(String secret) {
		try {
			return Base64.getDecoder().decode(secret);
		} catch (IllegalArgumentException exception) {
			return secret.getBytes(StandardCharsets.UTF_8);
		}
	}
}
