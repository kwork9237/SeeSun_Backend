package com.seesun.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.admin.AdminDTO;
import com.seesun.dto.admin.MentoRequestListDTO;
import com.seesun.dto.notification.NotificationDTO;
// [추가] 건의사항 DTO만 import
import com.seesun.dto.suggestion.SuggestionDTO;

import com.seesun.service.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;    

    // 관리자 메인
    @GetMapping("/dashboard-stats")
    public ResponseEntity<AdminDTO> getDashboardStats() {
        System.out.println("관리자 메인처리 요청");
        AdminDTO stats = adminService.getDashboardStats();
        System.out.println("stats:"+ stats);
        
        return ResponseEntity.ok(stats);
    }

    // 미승인 목록 조회
    @GetMapping("/pending")
    public List<MentoRequestListDTO> getPendingMentoRequests() {
        System.out.println("미승인 목록:" + adminService.getPendingList());
        return adminService.getPendingList();
    }

    // 멘토 승인 처리
    @PostMapping("/approve")
    public String approveMento(@RequestBody Map<String, Integer> payload) {
        System.out.println("멘토 승인 요청");
        System.out.println("reqid:" + payload.get("reqId"));
        
        int reqId = payload.get("reqId");
        boolean isApproved = adminService.approveRequest(reqId);
        System.out.println("승인 여부:" + isApproved);
        
        return isApproved ? "SUCCESS" : "FAIL";
    }
    
  
    // 건의사항 조회
    @GetMapping("/suggestions")
    public ResponseEntity<List<SuggestionDTO>> getSuggestions() {
        System.out.println("건의 사항 목록 조회 요청");
        // AdminService에 getSuggestions() 메서드가 있어야 합니다.
        List<SuggestionDTO> list = adminService.getSuggestions();
        return ResponseEntity.ok(list);
    }
    
    // 공지사항 목록
    @GetMapping("/notices")
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        System.out.println("공지사항 목록 조회 요청");
        List<NotificationDTO> list = adminService.getAllNotifications();
        System.out.println("공지사항 목록:"+ list);
        return ResponseEntity.ok(list);
    }
    
    // 공지 사항 작성
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

    // 공지사항 삭제
    @DeleteMapping("/notices/{ntId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("ntId") Long ntId) {
        System.out.println("공지사항 삭제 요청: " + ntId);
        adminService.deleteNotification(ntId);
        return ResponseEntity.ok("SUCCESS");
    }

}