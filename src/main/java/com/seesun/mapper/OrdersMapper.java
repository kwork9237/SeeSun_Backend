package com.seesun.mapper;

import com.seesun.dto.OrdersDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface OrdersMapper {
    // 조회용 (Map으로 받음)
    Map<String, Object> findLectureById(Long leId);
    Map<String, Object> findMemberById(Long mbId);

    // 주문 생성 (INSERT)
    void insertOrder(OrdersDTO ordersDTO);

    // 결제 승인 (UPDATE)
    void updatePaymentSuccess(OrdersDTO ordersDTO);
}
