package com.seesun.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seesun.service.order.OrdersService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.seesun.security.userdetail.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    // Mapper 대신 Service를 부릅니다!
    private final OrdersService ordersService;

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> createOrder(
        @AuthenticationPrincipal CustomUserDetails user, // 토큰에서 유저 정보 꺼내기
        @RequestBody Map<String, Long> requestData
    ) {
        // 토큰이 위조되지 않았다면, user 안에 진짜 mbId가 들어있습니다.
        Long mbId = user.getMbId(); // (CustomUserDetails에 getMbId 메서드 필요)
        Long leId = requestData.get("le_id");

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

    // 결제 기록 호출
    @GetMapping("/history/{mbId}")
    public ResponseEntity<List<Map<String, Object>>> getPaymentHistory(@PathVariable Long mbId) {
        // 서비스 또는 매퍼 호출
        List<Map<String, Object>> historyList = ordersService.getPaymentHistory(mbId);
        return ResponseEntity.ok(historyList);
    }
}