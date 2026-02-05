package com.seesun.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {	
	// Jwt Secret Key
	@Value("${jwt.secret}")
    private String secretKey;
	
	// Jwt Expiration Time (MS)
	@Value("${jwt.expiration}")
    private long expirationTime;
	
	// JWT 토큰 생성
	// 회원 ID, 유저명만 토큰에 저장 (정보 최소화)
	public String createToken(String username, Long mbId, short mbTypeId) {
		String role = switch (mbTypeId) {
	        case 0 -> "ROLE_ADMIN";
	        case 1 -> "ROLE_MENTEE";
	        case 2 -> "ROLE_MENTOR";
	        default -> "ROLE_GUEST";
	    };
		
		
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("mbId", mbId);
		claims.put("role", role);

		// 현재 시각, 만료 시각
		Date now = new Date();
		Date expTime = new Date(now.getTime() + expirationTime);
		
		// JWT 토큰 반환
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expTime)
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}
	
	// JWT 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.build()
				.parseClaimsJws(token);			
			return true;
		} catch(JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	// JWT 토큰 파싱
	public Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder()
	            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
	            .build()
	            .parseClaimsJws(token)
	            .getBody(); 
		} catch(Exception e) {
			return null;
		}
	}
	
	// 위의 파싱 메서드 기반으로 다른 data 가져오기 가능.
	
	// Claims에서 MemberId 가져오기
	public Long getMemberId(String token) {
        return getClaims(token).get("mbId", Long.class);
    }
	
	// 토큰에서 유저이름 가져오기
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}
	
	// 권한 가져오기
	public String getRole(String token) {
		return getClaims(token).get("role", String.class);
	}
}
