package com.seesun.config.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.seesun.security.filter.JwtAuthenticationFilter;
import com.seesun.security.jwt.JwtTokenProvider;
import com.seesun.security.userdetail.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService userDetailService;
	
	// 비밀번호 암호화
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 인증
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
	// CORS 설정
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
    	    "http://localhost:3000",          // 로컬 개발용
    	    "https://du-project.kro.kr"		  // 실제 서비스 도메인은 추후 적용 필요. (26 01 15)
    	));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Set-Cookie");
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	// 필터링
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CORS & CSRF 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. 세션 비활성화 (JWT 사용 시 필수)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. 기본 로그인 비활성화
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // 4. 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // Pre-flight Request 허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                // 허용된 API 경로
                // 결제 관련 API
                .requestMatchers("/api/orders/**").permitAll()

                // 마이페이지 API
                .requestMatchers("/api/member/**").permitAll()

                // 채팅 SSE 전용 API + 인증 없이 허용
                .requestMatchers("/api/seesun/session/chat/**").permitAll()
                // WebSocket STOMP 허용
                .requestMatchers("/ws/**", "/ws").permitAll()
                .requestMatchers("/pub/**", "/sub/**").permitAll()

                // 비로그인시 모든 API 경로 허용 (임시 조치)
                .requestMatchers("/api/**").permitAll()
                
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )

        	// 5. JWT 필터 등록
        	.addFilterBefore(
        		new JwtAuthenticationFilter(jwtTokenProvider, userDetailService), 
        		UsernamePasswordAuthenticationFilter.class
    		);

        return http.build();
    }
}
