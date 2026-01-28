package com.seesun.service.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.admin.AdminDTO;
import com.seesun.dto.admin.MentoRequestListDTO;
import com.seesun.dto.notification.NotificationDTO;
// [필수] SuggestionDTO import 확인
import com.seesun.dto.suggestion.SuggestionDTO; 

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
    public AdminDTO getDashboardStats() {
        AdminDTO stats = new AdminDTO();
        stats.setNewMentorCount(adminMapper.countNewMentorRequests());
        stats.setReportedLectureCount(adminMapper.countReportedLectures());
        stats.setInquiryCount(adminMapper.countSuggestions());
        
        return stats;
    }

    // 미승인 목록 가져오기
    public List<MentoRequestListDTO> getPendingList() {
        return adminMapper.selectPendingRequests();
    }

    // 멘토승인 처리하기
    public boolean approveRequest(int reqId) {
        return adminMapper.updateRequestStatus(reqId) > 0;
    }

    // 강의 신고
    
    // ---------------------------------------------------------
    // [추가됨] 건의사항 목록 조회
    // ---------------------------------------------------------
    public List<SuggestionDTO> getSuggestions() {
        return adminMapper.selectSuggestionList();
    }
    public SuggestionDTO getSuggestionDetail(Long sgId) {
        // 1. 조회수 증가
        adminMapper.increaseSuggestionViewCount(sgId);	
        
        // 2. 상세 데이터 조회 후 반환
        return adminMapper.selectSuggestionDetail(sgId);
    }
    
    // 1. 공지사항 전체 목록 조회
    public List<NotificationDTO> getAllNotifications() {
        return adminMapper.selectNotificationList();
    }
    
    // 2. 공지 사항 작성
    public void createNotification(NotificationDTO dto) {
        adminMapper.insertNotification(dto);
    }

    // 3. 공지사항 상세 조회 + 조회수 증가
    public NotificationDTO getNotificationDetail(Long ntId) {
        // 1. 조회수 증가 (DB 업데이트)
        adminMapper.increaseViewCount(ntId);
        // 2. 상세 정보 가져오기 (조회 후 반환)
        return adminMapper.selectNotificationDetail(ntId);
    } 

    // 4. 공지사항 수정 기능
    public void updateNotification(NotificationDTO dto) {
        adminMapper.updateNotification(dto);
    }
    
    // 5. 공지사항 삭제 기능
    public void deleteNotification(Long ntId) {
        adminMapper.deleteNotification(ntId);
    }
    
}