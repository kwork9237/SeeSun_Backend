package com.seesun.controller.mypage;

import com.seesun.dto.mypage.DashboardDTO;
import com.seesun.mapper.lecture.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// [보안/토큰 적용 시 주석 해제할 Import]
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import com.seesun.security.userdetail.CustomUserDetails;

import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor

public class DashboardController {
    private final DashboardMapper dashboardMapper;

    /* ============================================================
    // [1] 미래 버전: 토큰 기반 (보안 적용 시 주석 해제 후 사용)
    // ============================================================
    @GetMapping("/home")
    public ResponseEntity<DashboardDTO> getDashboardHome(@AuthenticationPrincipal CustomUserDetails user) {

        // 토큰에서 내 ID 꺼내기
        Long mbId = user.getMbId();
        return getDashboardData(mbId); // 아래 공통 로직 호출
    }
    */

    // ============================================================
    // [2] 현재 버전: 개발용 (ID 직접 입력)
    // ============================================================
    @GetMapping("/home/{mbId}")
    public ResponseEntity<DashboardDTO> getDashboardHome(@PathVariable Long mbId) {
        return getDashboardData(mbId); // 공통 로직 호출
    }

    // ============================================================
    // [공통 로직] ID만 주면 데이터를 조립해서 리턴하는 메서드 (2번을 지우고 1번 주석을 살리면 작동)
    // ============================================================
    private ResponseEntity<DashboardDTO> getDashboardData(Long mbId) {
        DashboardDTO dashboard = new DashboardDTO();

        // 메인 강의 정보 조회 (Hybrid Logic)
        Map<String, Object> recent = dashboardMapper.getRecentLecture(mbId);

        if (recent != null) {
            dashboard.setLeId(Long.parseLong(String.valueOf(recent.get("le_id"))));
            dashboard.setTitle((String) recent.get("title"));
            dashboard.setMentorName((String) recent.get("mentor_name"));

            // 숫자형 데이터 안전 파싱 (null이면 0 처리)
            Object costObj = recent.get("cost");
            dashboard.setCost(costObj != null ? Integer.parseInt(String.valueOf(costObj)) : 0);

            Object levelObj = recent.get("difficulty_level");
            dashboard.setDifficultyLevel(levelObj != null ? Integer.parseInt(String.valueOf(levelObj)) : 1);

            // 날짜 정보가 있으면 넣고, 없으면 null 상태 유지
            if (recent.get("next_date") != null) {
                dashboard.setNextLectureDate((String) recent.get("next_date"));
                dashboard.setNextLectureTime((String) recent.get("next_time"));
            }
        } else {
            dashboard.setTitle("수강 신청 내역이 없습니다.");
        }

        return ResponseEntity.ok(dashboard);
    }
}
