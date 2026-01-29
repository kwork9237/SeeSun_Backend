package com.seesun.mapper.admin;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // [필수] Param 임포트 확인

import com.seesun.dto.admin.MemberManageDTO;
import com.seesun.dto.admin.MentoRequestListDTO;
import com.seesun.dto.notification.NotificationDTO;
import com.seesun.dto.suggestion.SuggestionDTO;

@Mapper
public interface AdminMapper {

	// 관리자 메인 통계
	int countNewMentorRequests();
	int countReportedLectures();
    int countSuggestions();
	
	// 멘토 미승인 목록
	List<MentoRequestListDTO> selectPendingRequests();

	// 멘토 승인 처리
	int updateRequestStatus(int reqId);
	
	// ---------------------------------------------------------
	// [수정 완료] 전체 회원 조회 (검색 기능: 키워드 + 타입)
    // ---------------------------------------------------------
    List<MemberManageDTO> selectAllMembers(@Param("keyword") String keyword, @Param("searchType") String searchType);
	
	// 건의사항 조회
	List<SuggestionDTO> selectSuggestionList();
	
	// 건의사항 상세보기
	void increaseSuggestionViewCount(Long sgId);
	SuggestionDTO selectSuggestionDetail(Long sgId);
	
	// 답변 존재 여부 확인
    int countAnswerBySgId(Long sgId);

    // 답변 수정
    void updateSuggestionAnswer(com.seesun.dto.suggestion.SuggestionAnswerDTO dto);
    
    // 답변 등록
    void insertSuggestionAnswer(com.seesun.dto.suggestion.SuggestionAnswerDTO dto);
    
    // 건의사항 삭제
    void deleteSuggestion(Long sgId);
	
	// 공지 사항
    List<NotificationDTO> selectNotificationList();

    NotificationDTO selectNotificationDetail(Long ntId);
	void insertNotification(NotificationDTO dto);
			
	void increaseViewCount(Long ntId);
	void updateNotification(NotificationDTO dto);
	void deleteNotification(Long ntId);
	
}