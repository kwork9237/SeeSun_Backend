package com.seesun.dto.lecture.session;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("lectureSessionHistory")
public class LectureSessionHistoryDTO {
	private Long history_id;
	private short room_id;
	private Long le_id;
	private Long mb_id;
	private short status;
}
