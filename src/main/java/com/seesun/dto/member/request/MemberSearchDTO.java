package com.seesun.dto.member.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

//회원 정보 조회시 사용 (마이페이지 사이드바, 프로필 설정)
@Getter
@Setter
@Alias("membersearch")
public class MemberSearchDTO {
    private Long mbId;            // 회원가입후 바로 사용하기 위함.
    @JsonProperty("email")        // Front에서는 Email과 매핑됨
    private String username;    // ID
    private String name;        // Name
    private String nickname;    // 강의에서 보여질 이름
    private String phone;        // Phone
    private LocalDateTime createdAt;
}

