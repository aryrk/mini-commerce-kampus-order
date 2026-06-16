package com.mini.commerce.kampus.aryo.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.commerce.kampus.aryo.order.dto.CancelOrder.CancelOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderRequest;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.PayOrder.PayOrderResponse;
import com.mini.commerce.kampus.aryo.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest payload) {
        CreateOrderResponse response = orderService.createOrder(payload);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DetailOrderResponse>> getAllOrder() {
        List<DetailOrderResponse> response = orderService.getAllOrder();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailOrderResponse> getOrderById(@PathVariable UUID id) {
        DetailOrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/pay")
    public ResponseEntity<PayOrderResponse> payOrder(@PathVariable UUID id) {
        PayOrderResponse response = orderService.payOrder(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable UUID id) {
        CancelOrderResponse response = orderService.cancelOrder(id);
        return ResponseEntity.ok(response);
    }
}
