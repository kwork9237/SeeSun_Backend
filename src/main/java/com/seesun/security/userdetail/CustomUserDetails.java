package com.seesun.security.userdetail;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seesun.dto.member.AuthenticationDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
	// 시리얼라이즈 경고 해결
	private static final long serialVersionUID = 1L;
	
	private Long mbId;
	private String username;
	private String password;
	private short mbTypeId;
	
	// DB에서 가져온 DTO를 CustomUserDetails 로 변환하는 생성자.
	public CustomUserDetails(AuthenticationDTO member) {
		this.mbId = member.getMb_id();
		this.username = member.getUsername();
		this.password = member.getPassword();
		this.mbTypeId = member.getMb_type_id();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role;
	    switch (this.mbTypeId) {
	        case 0: 
	            role = "ROLE_ADMIN"; // 관리자
	            break;
	        case 1: 
	            role = "ROLE_MENTEE"; // 멘토
	            break;
	        case 2: 
	            role = "ROLE_MENTO"; // 멘티
	            break;
	        default: 
	            role = "ROLE_GUEST"; // 비회원
	            break;
	    }
	    
	    return List.of(new SimpleGrantedAuthority(role));
	}
}