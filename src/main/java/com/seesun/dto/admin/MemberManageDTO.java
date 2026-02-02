package com.seesun.dto.admin;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MemberManageDTO {
    private Long mbId;          // 회원 번호 (PK)
    private String username;    // 아이디 (DB컬럼: username)
    private String name;        // 이름
    private String nickname;    // 닉네임
    private String phone;       // 전화번호 (DB컬럼: phone)
    private int mbTypeId;       // 회원 유형 (0:관리자, 1:일반, 2:멘토)
    private LocalDateTime createdAt; // 가입일
}