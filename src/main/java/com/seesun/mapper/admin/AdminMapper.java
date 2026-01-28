package com.seesun.mapper.admin;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.seesun.dto.admin.MentoRequestListDTO;
import com.seesun.dto.notification.NotificationDTO;
// [필수 추가] 건의사항 DTO Import
import com.seesun.dto.suggestion.SuggestionDTO;

@Mapper
public interface AdminMapper {

	// 관리자 메인
	int countNewMentorRequests();
	int countReportedLectures();
	
	// 멘토 미승인 목록
	List<MentoRequestListDTO> selectPendingRequests();

	// 멘토 승인 처리
	int updateRequestStatus(int reqId);
	
	// 건의사항 조회
    // [추가됨] XML의 id="selectSuggestionList"와 이름이 같아야 합니다.
	List<SuggestionDTO> selectSuggestionList();
	
	// 공지 사항
    List<NotificationDTO> selectNotificationList();

    NotificationDTO selectNotificationDetail(Long ntId);
	void insertNotification(NotificationDTO dto);
			
	void increaseViewCount(Long ntId);
	void updateNotification(NotificationDTO dto);
	void deleteNotification(Long ntId);
	
}