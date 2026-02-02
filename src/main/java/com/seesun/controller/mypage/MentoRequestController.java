package com.seesun.controller.mypage;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.member.MemberService; // ★ 이미 있는 MemberService 사용
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mento")
@RequiredArgsConstructor
public class MentoRequestController { // ★ 이름 수정 완료

    private final MemberService memberService;

    // 멘토 신청 API
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 로그인 유저 정보
            @RequestPart("details") String details,                 // 신청 사유
            @RequestPart("file") MultipartFile file                 // 증빙 파일
    ) {
        // 멤버ID 가져오기
        Long mbId = userDetails.getMbId();

        // MemberService가 내부적으로 (파일저장 + DB저장) 모두 처리
        memberService.mentorRequest(mbId, details, file);

        return ResponseEntity.ok("멘토 신청이 성공적으로 접수되었습니다.");
    }
}
