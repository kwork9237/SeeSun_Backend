package com.seesun.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.admin.AdminDTO;
import com.seesun.dto.admin.MemberManageDTO;
import com.seesun.dto.admin.MentoRequestListDTO;
import com.seesun.dto.notification.NotificationDTO;
import com.seesun.dto.suggestion.SuggestionDTO;
import com.seesun.service.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;    

    // 관리자 메인 통계
    @GetMapping("/dashboard-stats")
    public ResponseEntity<AdminDTO> getDashboardStats() {
        System.out.println("관리자 메인처리 요청");
        AdminDTO stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // 미승인 멘토 신청 목록 조회
    @GetMapping("/pending")
    public List<MentoRequestListDTO> getPendingMentoRequests() {
        System.out.println("미승인 목록 조회 요청");
        return adminService.getPendingList();
    }

    // 멘토 승인 처리
    @PostMapping("/approve")
    public String approveMento(@RequestBody Map<String, Integer> payload) {
        System.out.println("멘토 승인 요청: " + payload.get("reqId"));
        int reqId = payload.get("reqId");
        boolean isApproved = adminService.approveRequest(reqId);
        return isApproved ? "SUCCESS" : "FAIL";
    }
    
 // [수정] 전체 회원 목록 조회 API
 
 // [수정] 전체 회원 목록 조회 (검색 기능 추가)
    @GetMapping("/members")
    public ResponseEntity<List<MemberManageDTO>> getAllMembers(
            @RequestParam(value = "keyword", required = false) String keyword) { // keyword 추가
        
        System.out.println("회원 목록 조회 요청 - 검색어: " + keyword);
        
        // 서비스로 keyword 전달
        List<MemberManageDTO> list = adminService.getAllMembers(keyword);
        
        return ResponseEntity.ok(list);
    }
    
    // ---------------- [건의사항 관련] ----------------s

    // 건의사항 목록 조회
    @GetMapping("/suggestions")
    public ResponseEntity<List<SuggestionDTO>> getSuggestions() {
        System.out.println("건의 사항 목록 조회 요청");
        List<SuggestionDTO> list = adminService.getSuggestions();
        return ResponseEntity.ok(list);
    }

    // 건의사항 상세 보기
    @GetMapping("/suggestions/{sgId}")
    public ResponseEntity<SuggestionDTO> getSuggestionDetail(@PathVariable("sgId") Long sgId) {
        System.out.println("건의 사항 상세 조회 요청: " + sgId);
        SuggestionDTO detail = adminService.getSuggestionDetail(sgId);
        return ResponseEntity.ok(detail);
    }

    // [수정됨] 건의사항 삭제 (파라미터 이름 명시)
    @DeleteMapping("/suggestions/{sgId}")
    public ResponseEntity<String> deleteSuggestion(@PathVariable("sgId") Long sgId) {
        System.out.println("건의사항 삭제 요청 ID: " + sgId);
        adminService.deleteSuggestion(sgId);
        return ResponseEntity.ok("DELETED");
    }
    
    // 답변 등록 및 수정
    @PostMapping("/suggestions/answers")
    public ResponseEntity<String> registerAnswer(@RequestBody com.seesun.dto.suggestion.SuggestionAnswerDTO dto) {
        System.out.println("답변 등록 요청: " + dto);
        adminService.registerAnswer(dto);
        return ResponseEntity.ok("SUCCESS");
    }
    
    // ---------------- [공지사항 관련] ----------------

    // 공지사항 목록
    @GetMapping("/notices")
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        System.out.println("공지사항 목록 조회 요청");
        List<NotificationDTO> list = adminService.getAllNotifications();
        return ResponseEntity.ok(list);
    }
    
    // 공지사항 작성
    @PostMapping("/notices")
    public ResponseEntity<String> createNotification(@RequestBody NotificationDTO dto) {
        System.out.println("공지사항 작성 요청: " + dto);
        adminService.createNotification(dto); 
        return ResponseEntity.ok("SUCCESS");
    }
    
    // 공지사항 상세 조회
    @GetMapping("/notices/{ntId}")
    public ResponseEntity<NotificationDTO> getNotificationDetail(@PathVariable("ntId") Long ntId) {
        System.out.println("공지사항 상세 조회 요청: " + ntId);
        NotificationDTO notice = adminService.getNotificationDetail(ntId);
        return ResponseEntity.ok(notice);
    }
    
    // 공지사항 수정
    @PutMapping("/notices/{ntId}")
    public ResponseEntity<String> updateNotification(@PathVariable("ntId") Long ntId, @RequestBody NotificationDTO dto) {
        System.out.println("공지사항 수정 요청: " + ntId);
        dto.setNtId(ntId);
        adminService.updateNotification(dto);
        return ResponseEntity.ok("SUCCESS");
    }

    // [수정됨] 공지사항 삭제 (건의사항 삭제와 겹치지 않게 수정함)
    @DeleteMapping("/notices/{ntId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("ntId") Long ntId) {
        System.out.println("공지사항 삭제 요청 ID: " + ntId);
        // AdminService에 deleteNotification 메서드가 있어야 합니다.
        adminService.deleteNotification(ntId);
        return ResponseEntity.ok("DELETED");
    }

} 