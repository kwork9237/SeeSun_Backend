package com.seesun.mapper.admin;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.seesun.dto.admin.MentoRequestDTO;
import com.seesun.dto.notification.NotificationDTO;

@Mapper
public interface AdminMapper {

	// 관리자 메인
	int countNewMentorRequests();
	int countReportedLectures();

	// 멘토 미승인 목록
	List<MentoRequestDTO> selectPendingRequests();

	// 멘토 승인 처리
	int updateRequestStatus(int reqId);
	
	// 공지 사항 (어노테이션 없이 메서드 이름만 남겨야 함 -> XML이 처리)
    List<NotificationDTO> selectNotificationList();

    NotificationDTO selectNotificationDetail(Long ntId);
	void insertNotification(NotificationDTO dto);
	
	
	
	

}