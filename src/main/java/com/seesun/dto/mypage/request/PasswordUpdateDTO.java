package com.seesun.dto.mypage.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateDTO {
	private String oldPassword;
	private String newPassword;
}
