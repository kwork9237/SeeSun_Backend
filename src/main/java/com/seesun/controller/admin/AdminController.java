package com.seesun.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.admin.AdminDto;
import com.seesun.dto.admin.MentoRequestDTO;
import com.seesun.service.admin.AdminService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
		private final AdminService adminService;	
	
		//관리자 메인
		@GetMapping("/dashboard-stats")
	    public ResponseEntity<AdminDto> getDashboardStats() {
			System.out.println("관리자 메인처리 요청");
	        AdminDto stats = adminService.getDashboardStats();
	        System.out.println("stats:"+ stats);
	        
	        return ResponseEntity.ok(stats);
	    }
		
    	//관리자 멘토 승인
		// 미승인 목록 조회
	    @GetMapping("/pending")
	    public List<MentoRequestDTO> getPendingMentoRequests() {
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
	    
	    //강의 신고

}
