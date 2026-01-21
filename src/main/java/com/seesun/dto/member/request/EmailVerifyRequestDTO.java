package com.seesun.dto.member.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequestDTO {
	private String email;
	private String code;
}
