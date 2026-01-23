package com.seesun.service.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.admin.AdminDto;
import com.seesun.dto.admin.MentoRequestDTO;
import com.seesun.mapper.admin.AdminMapper;
import com.seesun.service.auth.MemberCredentialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminMapper adminMapper;
	private final PasswordEncoder pwEncoder;
	private final MemberCredentialService credentialService;

	// 관리자 메인
	public AdminDto getDashboardStats() {
		AdminDto stats = new AdminDto();
		// 각각의 카운트를 DB에서 조회하여 DTO에 설정
		stats.setNewMentorCount(adminMapper.countNewMentorRequests());
		stats.setReportedLectureCount(adminMapper.countReportedLectures());
//	      stats.setInquiryCount(adminMapper.countUnansweredInquiries());

		return stats;
	}

	// 미승인 목록 가져오기
	public List<MentoRequestDTO> getPendingList() {
		return adminMapper.selectPendingRequests();
	}

	// 멘토승인 처리하기
	public boolean approveRequest(int reqId) {
		return adminMapper.updateRequestStatus(reqId) > 0;
		
		
		
	//강의 신고
	
	//건의사항
	}

}
