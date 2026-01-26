package com.seesun.dto.admin;

import lombok.Data;

@Data	
public class MentoRequestDTO {
	private int reqId;        // req_id (PK)
    private String mbId;      // mb_id (신청자 ID)
    private String details;   // details (요청 내용)
    private String attachment;// attachment (첨부파일)
    private int reqState;     // req_state (0:미승인, 1:승인)
}
