package com.seesun.service.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.admin.AdminDto;
import com.seesun.dto.admin.MentoRequestDTO;
import com.seesun.dto.notification.NotificationDTO;
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
        stats.setNewMentorCount(adminMapper.countNewMentorRequests());
        stats.setReportedLectureCount(adminMapper.countReportedLectures());
        // stats.setInquiryCount(adminMapper.countUnansweredInquiries());
        return stats;
    }

    // 미승인 목록 가져오기
    public List<MentoRequestDTO> getPendingList() {
        return adminMapper.selectPendingRequests();
    }

    // 멘토승인 처리하기
    public boolean approveRequest(int reqId) {
        return adminMapper.updateRequestStatus(reqId) > 0;
    }

    // 강의 신고
    
    // 건의사항
    
    // 공지 사항
    
    // 1. 공지사항 전체 목록 조회
    public List<NotificationDTO> getAllNotifications() {
        return adminMapper.selectNotificationList();
    }
    
    // 공지 사항 작성
    public void createNotification(NotificationDTO dto) {
        adminMapper.insertNotification(dto);
    }

    // [수정] 공지사항 상세 조회 + 조회수 증가
    public NotificationDTO getNotificationDetail(Long ntId) {
        
        // 1. 조회수 증가 (DB 업데이트)
        adminMapper.increaseViewCount(ntId);
        
        // 2. 상세 정보 가져오기 (조회 후 반환)
        return adminMapper.selectNotificationDetail(ntId);
    }
}