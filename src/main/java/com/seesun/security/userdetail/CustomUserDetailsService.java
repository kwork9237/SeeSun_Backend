package com.seesun.security.userdetail;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seesun.dto.member.AuthenticationDTO;
import com.seesun.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MemberMapper mbMapper;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthenticationDTO mb = mbMapper.getAuthenticationByUsername(username);

		if(mb == null)
			throw new UsernameNotFoundException("사용자 조회 실패 : " + username);
		
		return new CustomUserDetails(
				mb.getMb_id(),
				mb.getUsername(),
				mb.getPassword(),
				mb.getMb_type_id()
			);
	}
}