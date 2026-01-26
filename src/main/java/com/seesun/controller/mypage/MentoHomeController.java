package com.seesun.controller.mypage;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.mapper.lecture.MentoHomeMapper;
// [협업 노트] 시큐리티 관련 클래스가 없다면 아래 import 2줄에서 에러가 날 수 있습니다.
// 아직 시큐리티 설정 전이라면 주석 처리해주세요.
import com.seesun.security.userdetail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [협업 노트] MentoHomeController
 * 작성자: Gemini
 * 설명: 멘토 마이페이지 대시보드 데이터(일정, 강의목록)를 제공하는 컨트롤러
 * * [Endpoint 구조]
 * 1. GET /api/mento/home       -> (Prod) 토큰(Header)을 이용해 내 정보를 찾음
 * 2. GET /api/mento/home/{id}  -> (Dev)  URL 파라미터로 내 정보를 찾음 (테스트용)
 */
@RestController
@RequestMapping("/api/mento") // 공통 기본 경로
@RequiredArgsConstructor
public class MentoHomeController {

    private final MentoHomeMapper mentoHomeMapper;

    // 1. [운영 환경용] 토큰 기반 조회 (Token Based)
    // 헤더 필수: Authorization: Bearer {token}
    // ==================================================================
   // @GetMapping("/home")
    //public ResponseEntity<?> getHomeDataWithToken(@AuthenticationPrincipal CustomUserDetails user) {

        // [Security] 토큰이 없거나 만료되면 Filter 단계에서 이미 403 에러가 발생함.
        // 여기까지 왔다면 인증된 사용자임.

        // CustomUserDetails에서 mbId 추출 (구조에 따라 user.getMbId() 일수도 있음)
        // 여기서는 User Entity를 꺼내서 ID를 가져오는 방식으로 작성
     //   Long mbId = user.getMember().getMbId();

        // 공통 로직 호출
      //  return getResponse(mbId);
  //  }

    // 2. [개발/테스트용] URL 파라미터 기반 조회 (Param Based)
    // 설명: 프론트엔드에서 아직 토큰 작업이 안 되었을 때 사용
    // ==================================================================
    @GetMapping("/home/{mbId}")
    public ResponseEntity<?> getHomeDataWithParam(@PathVariable Long mbId) {
        // 공통 로직 호출
        return getResponse(mbId);
    }

    private ResponseEntity<?> getResponse(Long mbId) {
        Map<String, Object> response = new HashMap<>();

        /* * 1. 스케줄 리스트 조회
         * - MentoHomeMapper.xml의 selectMentoSchedules 실행
         * - 날짜, 시간, 입장 버튼 등에 사용
         */
        List<LectureDTO> schedules = mentoHomeMapper.selectMentoSchedules(mbId);
        response.put("schedules", schedules);

        /* * 2. 내 강의 목록 조회 (+수강생 현황 집계 포함)
         * - MentoHomeMapper.xml의 selectMentoLectures 실행
         * - Orders 테이블 카운트(currentStudents)와 Schedule 테이블(maxStudents) 포함
         * - 프론트엔드에서 'myCreatedLectures'라는 키값으로 찾을 수 있게 저장
         */
        List<LectureDTO> myLectures = mentoHomeMapper.selectMentoLectures(mbId);
        // 프론트엔드 코드(TeachingSection)와 맞추기 위해 키 이름을 'myCreatedLectures'로 설정
        response.put("myCreatedLectures", myLectures);

        return ResponseEntity.ok(response);
    }
}