package com.seesun.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import com.seesun.security.jwt.JwtTokenProvider;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.security.userdetail.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Auth 헤더 Read
		String header = request.getHeader("Authorization");
		
		// Bearer Token 확인
		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// token 내용 가져오기
		String token = header.substring(7);
		try {
			// 유효하지 않은 토큰
			if(!jwtTokenProvider.validateToken(token)) {
				filterChain.doFilter(request, response);
				return;
			}
			
			// Token 에서 Username 추출
			String username = jwtTokenProvider.getUsername(token);

			// userDetail 로드
			CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
			
			// 인증 개체 생성
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities()
				);
			
			// 인증 개체 저장
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch(Exception e) {
			// 예외는 무조건 반환
			throw e;
		}
		
		filterChain.doFilter(request, response);
	}
}
