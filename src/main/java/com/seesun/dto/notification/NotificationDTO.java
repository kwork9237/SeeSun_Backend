package com.seesun.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
	private Long ntId;          // nt_id (No, 번호)
    private String mbId;        // mb_id (Writer, 작성자 ID)
    private String title;       // title (Title, 제목)
    private String content;     // content (내용)
    private int viewCount;      // view_count (Views, 조회수)
    private LocalDateTime createdAt;  // created_at (Date, 작성일)
    private LocalDateTime modifiedAt; // modified_at (수정일)

}
					