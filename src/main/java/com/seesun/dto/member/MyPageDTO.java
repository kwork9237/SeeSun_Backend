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
	private String username;			// ID (not update)
	private String name;				// name
	private String nickname;			// Lecture_name (강의화면에서 보여짐)
	private String phone;				// Phone
	private String profile_icon;		// remove able
	private short mb_type_id;			// Admin, Mento, Mentee
	private LocalDateTime created_at;
}
