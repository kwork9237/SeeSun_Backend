package com.seesun.mapper.order;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.order.OrdersDTO;

import java.util.List;
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

    // 주문 번호로 주문 정보(누가, 뭘 샀는지) 조회
    OrdersDTO findOrderByOrderId(String orderId);

    // 수강 신청 테이블(member_enrollment)에 데이터 넣기
    void insertEnrollment(@Param("mb_id") Long mbId, @Param("le_id") Long leId);

    // 결제 정보 가져오기
    List<Map<String, Object>> getPaymentHistory(Long mbId);

    // 현재 결제 완료된 인원수 조회
    int countActiveOrdersByLeId(Long leId);

    // 강의의 최대 정원 조회
    int getMaxStudentsByLeId(Long leId);

    // 결제 성공 시 해당 강의의 모든 스케줄 현재 인원 +1 업데이트
    void updateAllScheduleCurrentStudents(Long leId);
}
