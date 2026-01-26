package com.seesun.controller.mypage;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.mapper.lecture.MenteeHomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentee") //
@RequiredArgsConstructor
public class MenteeHomeController {

    private final MenteeHomeMapper menteeHomeMapper;

    @GetMapping("/home/{mbId}")
    public ResponseEntity<?> getHomeData(@PathVariable Long mbId) {
        Map<String, Object> response = new HashMap<>();

        // 1. 스케줄 (내가 신청한 강의의 시간표)
        List<LectureDTO> schedules = menteeHomeMapper.selectMenteeSchedules(mbId);
        response.put("schedules", schedules);

        // 2. 강의 목록 (내가 수강신청한 강의들)
        List<LectureDTO> myLectures = menteeHomeMapper.selectMenteeLectures(mbId);
        response.put("myLectures", myLectures);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 추후 토큰 이용시 사용
    /*
    @GetMapping("/home")
    public ResponseEntity<?> getHomeDataWithToken(@AuthenticationPrincipal CustomUserDetails user) {

        // 1. 토큰에서 로그인한 사람의 ID를 꺼낸다.
        Long mbId = user.getMbId();

        Map<String, Object> response = new HashMap<>();

        // 1. 스케줄
        List<LectureDTO> schedules = menteeHomeMapper.selectMenteeSchedules(mbId);
        response.put("schedules", schedules);

        // 2. 강의 목록
        List<LectureDTO> myLectures = menteeHomeMapper.selectMenteeLectures(mbId);
        response.put("myLectures", myLectures);

        return ResponseEntity.ok(response);
    }
    */


}
