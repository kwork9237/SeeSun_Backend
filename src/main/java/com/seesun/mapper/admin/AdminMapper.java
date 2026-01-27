package com.seesun.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seesun.dto.admin.MentoRequestListDTO;


@Mapper
public interface AdminMapper {

	// 관리자 메인
	// 3가지 통계를 한 번에 가져오거나, 각각 가져올 수 있습니다.
	// 여기서는 각각 조회하여 DTO를 조립하는 방식을 사용합니다.
	int countNewMentorRequests(); // 멘토 신청 카운트

	int countReportedLectures(); // 신고 강의 카운트
	// int countUnansweredInquiries(); // 미답변 건의사항 카운트ㅉ

	// 관리자 멘토 승인
	// 멘토 미승인 목록
	List<MentoRequestListDTO> selectPendingRequests(); 

	// 멘토 승인 처리
	int updateRequestStatus(int reqId); 
	
	
	//강의 신고
	
	//건의 사항

}
