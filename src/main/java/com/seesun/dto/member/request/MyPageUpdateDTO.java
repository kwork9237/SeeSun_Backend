package com.seesun.dto.member.request;

import com.seesun.dto.member.MyPageDTO;

import lombok.Getter;
import lombok.Setter;

// 마이페이지 업데이트할 경우 RequestBody는 1개로 제한됨.
@Getter
@Setter
public class MyPageUpdateDTO {
	private String password;
	private MyPageDTO myPageData;
}
