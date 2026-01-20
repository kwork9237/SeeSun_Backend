package com.seesun.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Alias("orders")
public class OrdersDTO {
    private String order_id;     // varchar(64) - 주문번호
    private Long mb_id;          // 구매자 PK
    private Long le_id;          // 강의 PK
    private Long cost;          // 결제 금액 (DB의 int/long 대응)
    private String title;       // 강의 제목
    private String payment_key;  // 결제 키 (초기엔 null)
    private Integer status;     // 1:대기, 2:승인, 3:취소, 4:환불
    private String method;      // 결제수단
    private LocalDateTime created_at;  // 결제생성 시간
    private LocalDateTime approved_at; // 결제승인 완료 시간
}
