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
    } // approveRequest 메서드 종료 괄호 위치 주의

    // 강의 신고
    
    // 건의사항
    
    // 공지 사항
    
    // [수정됨] 1. 공지사항 전체 목록 조회
    // static 제거, Object 선언 제거, 메서드 이름 맞춤(findAll -> selectNotificationList)
    public List<NotificationDTO> getAllNotifications() {
        return adminMapper.selectNotificationList();
    }

    // [수정됨] 2. 공지사항 상세 조회
    // static 제거, 메서드 이름 맞춤(findById -> selectNotificationDetail)
    public NotificationDTO getNotificationDetail(Long ntId) {
        return adminMapper.selectNotificationDetail(ntId);
    }

}