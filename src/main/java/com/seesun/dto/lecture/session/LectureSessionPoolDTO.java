package com.seesun.dto.lecture.session;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("lectureSessionPool")
public class LectureSessionPoolDTO {
	private short room_id;		// 방 고유 ID (10000 ~ 19999)
	private short status;		// 방 할당 정보
}
