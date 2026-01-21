package com.seesun.controller;

import com.seesun.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    // Mapper 대신 Service를 부릅니다!
    private final OrdersService ordersService;

    // 1. [결제 요청] 주문 생성
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Long> requestData) {
        Long mbId = requestData.get("mb_id");
        Long leId = requestData.get("le_id");

        // "서비스야, 주문 좀 만들어와!"
        Map<String, Object> responseData = ordersService.createOrder(mbId, leId);

        return ResponseEntity.ok(responseData);
    }

    // 2. [결제 승인] 토스 API 호출
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody Map<String, String> requestData) {
        try {
            String paymentKey = requestData.get("paymentKey");
            String orderId = requestData.get("orderId");
            String amount = requestData.get("amount");

            // "서비스야, 토스한테 승인 요청하고 DB 정리해!"
            ordersService.confirmPayment(paymentKey, orderId, amount);

            return ResponseEntity.ok("결제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("결제 승인 실패: " + e.getMessage());
        }
    }
}