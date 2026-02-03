package com.seesun.dto.lecture.session;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("activateSession")
public class ActivateSessionDTO {
	private short room_id;
	private Long mb_id;
	private Long le_id;
	private Long history_id;
	private String uuid;
	private short status;
}
