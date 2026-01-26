package com.seesun.service.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seesun.dto.order.OrdersDTO;
import com.seesun.mapper.order.OrdersMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersMapper ordersMapper;

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    // 1. 주문 생성 로직 (비즈니스 로직)
    @Transactional // DB 작업하다가 에러나면 자동 취소(롤백)
    public Map<String, Object> createOrder(Long mbId, Long leId) {
        // 강의 및 유저 정보 조회
        Map<String, Object> lectureInfo = ordersMapper.findLectureById(leId);
        Map<String, Object> memberInfo = ordersMapper.findMemberById(mbId);

        if (lectureInfo == null) throw new RuntimeException("강의 정보가 존재하지 않습니다.");
        if (memberInfo == null) throw new RuntimeException("유저 정보가 존재하지 않습니다.");

        String realTitle = (String) lectureInfo.get("title");
        Long realCost = Long.valueOf(String.valueOf(lectureInfo.get("cost")));
        String orderId = "ORD_" + UUID.randomUUID().toString();

        // DTO 생성 및 DB 저장
        OrdersDTO order = new OrdersDTO();
        order.setOrder_id(orderId);
        order.setMb_id(mbId);
        order.setLe_id(leId);
        order.setCost(realCost);
        order.setTitle(realTitle);
        order.setStatus(1); // 1: 대기 상태

        ordersMapper.insertOrder(order);
        System.out.println("✅ Service: 주문 대기 생성 완료 -> " + orderId);

        // 결과 반환 (프론트엔드에 필요한 정보만)
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("amount", realCost);
        response.put("orderName", realTitle);
        response.put("customerName", memberInfo.get("customerName"));

        return response;
    }

    // 2. 결제 승인 요청 로직 (외부 API 통신)
    @Transactional
    public void confirmPayment(String paymentKey, String orderId, String amount) throws Exception {
        // 시크릿 키 인코딩
        String secretKey = tossSecretKey + ":";
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));

        // 요청 JSON 만들기
        String jsonBody = String.format("{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%s}", paymentKey, orderId, amount);

        // 토스 API 호출
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", encodedAuth)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        // 성공 시 DB 업데이트
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());
            String method = jsonNode.get("method").asText();

            OrdersDTO updateOrder = new OrdersDTO();
            updateOrder.setOrder_id(orderId);
            updateOrder.setPayment_key(paymentKey);
            updateOrder.setMethod(method);

            ordersMapper.updatePaymentSuccess(updateOrder);

            // 수강신청 조회 테이블 값 삽입을 위한 정보 추출
            OrdersDTO orderInfo = ordersMapper.findOrderByOrderId(orderId);

            // 수강신청 조회 테이블 값 삽입
            if(orderInfo != null){
                ordersMapper.insertEnrollment(orderInfo.getMb_id(), orderInfo.getLe_id());
            }

            System.out.println("✅ Service: 결제 승인 및 DB 업데이트 완료 -> " + orderId);
        } else {
            System.err.println("❌ Service: 승인 실패 응답 -> " + response.body());
            throw new RuntimeException("토스 결제 승인 실패: " + response.body());
        }
    }

    // OrdersService.java 내부

    // [결제 내역 조회]
    public List<Map<String, Object>> getPaymentHistory(Long mbId) {
        // Mapper한테 "DB에서 긁어와!" 시키기
        return ordersMapper.getPaymentHistory(mbId);
    }
}
