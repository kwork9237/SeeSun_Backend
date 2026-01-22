package com.seesun.service.mypage;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.networknt.schema.OutputFormat.List;
import com.seesun.dto.admin.AdminDto;
import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.mapper.member.MemberMapper;
import com.seesun.service.auth.MemberCredentialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final MemberMapper memberMapper;
	private final PasswordEncoder pwEncoder;
	private final MemberCredentialService credentialService;
	
	// 회원정보 수정
	@Transactional
	public void updateMemberData(Long mbId, MyPageUpdateDTO data) {
		credentialService.checkPassword(mbId, data.getPassword());
		
		try {
			memberMapper.updateMemberInfoByMbId(mbId, data.getMyPageData());
		} catch(DuplicateKeyException e) {
			throw new GlobalException(ErrorCode.DUPLICATE_MEMBER_DATA);
		}
	}
	
	// 비밀번호 수정
	@Transactional
	public void updateMemberPassword(Long mbId, PasswordUpdateDTO data) {
		credentialService.checkPassword(mbId, data.getOldPassword());
		memberMapper.updatePasswordByMbId(mbId, pwEncoder.encode(data.getNewPassword()));
	}
	
	
}
