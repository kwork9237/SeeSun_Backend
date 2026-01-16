package com.seesun.dto.member;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

// 마이페이지 주요 데이터 (변경 가능)
@Getter
@Setter
@Alias("myPage")
public class MyPageDTO {
	private String username;
	private String name;
	private String nickname;
	private String phone;
	private String profile_icon;
	private short mb_type_id;
	private LocalDateTime created_at;
}
