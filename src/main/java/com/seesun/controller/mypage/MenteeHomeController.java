package com.seesun.controller.mypage;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.mapper.lecture.MenteeHomeMapper;
import com.seesun.security.userdetail.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentee") //
@RequiredArgsConstructor
public class MenteeHomeController {

    private final MenteeHomeMapper menteeHomeMapper;

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

}
