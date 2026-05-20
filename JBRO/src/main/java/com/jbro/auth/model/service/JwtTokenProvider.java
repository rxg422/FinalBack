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
	private final Key refreshSecretKey;
	private final long expirationMillis;
	private final long refreshExpirationMillis;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.refresh-secret}") String refreshSecret,
		@Value("${jwt.access-token-expiration-millis:3600000}") long 
		expirationMillis,
		@Value("${jwt.refresh-token-expiration-millis:604800000}") long
		refreshExpirationMillis //7일
	) {
        this.secretKey = Keys.hmacShaKeyFor(resolveSecretBytes(secret));
        this.refreshSecretKey = Keys.hmacShaKeyFor(resolveSecretBytes(refreshSecret)); 
        this.expirationMillis = expirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;  
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
			.claim("role", member.getRole() != null ? member.getRole() : "USER")
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(expiresAt))
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// ========== 리프레시 토큰 생성 ==========
    public String createRefreshToken(MemberVo member) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(refreshExpirationMillis);

        return Jwts.builder()
            .setSubject(String.valueOf(member.getId()))
            .claim("memberId", member.getId())
            .claim("role", member.getRole() != null ? member.getRole() : "USER")
            .claim("type", "refresh")  // 리프레시 토큰임을 표시
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiresAt))
            .signWith(refreshSecretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // ========== 리프레시 토큰으로 새 액세스 토큰 생성 ==========
    public String refreshAccessToken(String refreshToken) {
        try {
            var claims = Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

            Long memberId = claims.get("memberId", Long.class);
            String role = claims.get("role", String.class);

            if (memberId == null) {
                throw new RuntimeException("유효하지 않은 리프레시 토큰입니다!");
            }

            // 새로운 액세스 토큰 생성을 위해 임시 MemberVo 생성
            MemberVo tempMember = new MemberVo();
            tempMember.setId(memberId);
            tempMember.setRole(role != null ? role : "USER");

            return createAccessToken(tempMember);
        } catch (Exception e) {
            throw new RuntimeException("리프레시 토큰 갱신 실패: " + e.getMessage());
        }
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
