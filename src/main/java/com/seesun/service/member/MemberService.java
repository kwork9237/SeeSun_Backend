package com.seesun.service.member;

import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.member.request.MentoRequestDTO;
import com.seesun.dto.member.response.LoginResponseDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.mapper.member.MemberMapper;
import com.seesun.security.jwt.JwtTokenProvider;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.auth.MemberCredentialService;
import com.seesun.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final AuthenticationManager authManager;
	private final MemberCredentialService credentialService;
	private final PasswordEncoder pwEncoder;
	private final JwtTokenProvider jwtProvider;
	private final MemberMapper memberMapper;
	private final FileService fileService;
	
	// 회원가입 (멘티)
	@Transactional
	public void insertMember(MemberJoinDTO data) {
		// 회원 데이터 저장
		try {
			// 비밀번호 암호화
			data.setPassword(pwEncoder.encode(data.getPassword()));
			
			// 회원 데이터 저장
			memberMapper.insertMember(data);
		} catch(DuplicateKeyException e) {
			throw new GlobalException(ErrorCode.DATABASE_INSERT_ERROR);
		}
	}
	
	// 히원가입 (멘토)
	// 굳이 분리한 이유는 서로 로직이 중복되거나 다르기 때문.
	public void insertMentor(MemberJoinDTO data, String intro, MultipartFile file) {
		insertMember(data);
		
		mentorRequest(data.getMbId(), intro, file);
	}
	
	// 회원탈퇴
	@Transactional
	public void deleteMember(Long mbId, String password) {
		credentialService.checkPassword(mbId, password);
		memberMapper.deleteMemberByMbId(mbId);
	}
	
	// 중복 검사 (회원가입에 사용)
	public boolean checkDuplicate(String field, String value) {
		// front에서는 email로 작업했으므로 back에서 동일 의미로 매핑
		// email >> username
		if ("email".equals(field)) {
	        field = "username";
	    }
		
		// 3개만 검사
	    if (!Set.of("username","nickname","phone").contains(field)) {
	        throw new GlobalException(ErrorCode.INVALID_REQUEST);
	    }
	    
	    return memberMapper.checkDuplicate(field, value) == 1;
	}
	
	// 로그인 요청
	public LoginResponseDTO loginRequest(LoginRequestDTO data) {
		LoginResponseDTO response = new LoginResponseDTO();
		
		try {
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
				);
	
			// auth 개체를 userdetail로 변환
			CustomUserDetails d = (CustomUserDetails) auth.getPrincipal();
	
			// 토큰 생성 및 반환 
			response.setAccessToken(jwtProvider.createToken(d.getUsername(), d.getMbId()));

			return response;
		} catch (AuthenticationException e) {
			throw new GlobalException(ErrorCode.IDPW_NOT_MATCH);
		}
	}
	
	// 멘토 승인 요청
	public void mentorRequest(Long mbId, String intro, MultipartFile file) {

		// 파일 유효성 검증
		if(file == null || file.isEmpty()) {
			throw new GlobalException(ErrorCode.FILE_EMPTY);
		}
		
		// 파일 데이터 저장
		Long fid = fileService.save(mbId, "MENTO_REQUEST", file);
		
		MentoRequestDTO r = new MentoRequestDTO();
		r.setMb_id(mbId);
		r.setDetails(intro);
		r.setFile_id(fid);
		
		// 멘토 요청에 등록
		memberMapper.insertMentoRequest(r);
		
	}

	// 2026 01 27 수정필요
	// 내 정보 조회
//	public MyPageDTO getMyPageInfo(Long mbId) {
//		return memberMapper.getMyPageInfo(mbId);
//	}
}
