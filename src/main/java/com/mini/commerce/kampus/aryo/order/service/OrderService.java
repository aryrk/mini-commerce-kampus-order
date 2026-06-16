package com.mini.commerce.kampus.aryo.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mini.commerce.kampus.aryo.order.dto.CancelOrder.CancelOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderRequest;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderItemListResponse;
import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.PayOrder.PayOrderResponse;
import com.mini.commerce.kampus.aryo.order.entity.Order;
import com.mini.commerce.kampus.aryo.order.entity.OrderItems;
import com.mini.commerce.kampus.aryo.order.enums.OrderStatus;
import com.mini.commerce.kampus.aryo.order.repository.OrderItemsRepository;
import com.mini.commerce.kampus.aryo.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CatalogClientService catalogClientService;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest payload){
        Order order = new Order();
        order.setCustomerName(payload.getCustomerName());
        order.setCustomerEmail(payload.getCustomerEmail());

        BigDecimal totalPrice = BigDecimal.ZERO;

        List<OrderItems> orderItems = new ArrayList<>();
        for (var item : payload.getItems()) {
            UUID productId = item.getProductId();
            var product = catalogClientService.getProductDetailById(productId);

            if (product == null || product.getStock() < item.getQuantity() || !product.getStatus().equals("ACTIVE")) {
                throw new RuntimeException("Product not available");
            }

            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProductId(productId);
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);

            catalogClientService.reduceProductStock(productId, item.getQuantity());
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setItems(orderItems);
        order.setTotalAmount(totalPrice);
        orderRepository.save(order);

        return CreateOrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    public List<DetailOrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        List<DetailOrderResponse> response = new ArrayList<>();

        for (var order : orders) {
            List<DetailOrderItemListResponse> items = new ArrayList<>();
            for (var item : order.getItems()) {
                items.add(DetailOrderItemListResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .build());
            }
            response.add(DetailOrderResponse.builder()
                    .id(order.getId())
                    .customerName(order.getCustomerName())
                    .customerEmail(order.getCustomerEmail())
                    .items(items)
                    .build());
        }
        return response;
    }

    public DetailOrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<DetailOrderItemListResponse> items = new ArrayList<>();
        for (var item : order.getItems()) {
            items.add(DetailOrderItemListResponse.builder()
                    .id(item.getId())
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .productPrice(item.getProductPrice())
                    .quantity(item.getQuantity())
                    .build());
        }
        return DetailOrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .items(items)
                .build();
    }

    public PayOrderResponse payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new RuntimeException("Order cannot be paid");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return PayOrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    public CancelOrderResponse cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new RuntimeException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (var item : order.getItems()) {
            catalogClientService.increaseProductStock(item.getProductId(), item.getQuantity());
        }

        return CancelOrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
