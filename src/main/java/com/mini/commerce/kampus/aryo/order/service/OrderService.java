package com.mini.commerce.kampus.aryo.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mini.commerce.kampus.aryo.order.dto.CancelOrder.CancelOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.CatalogService.product.ProductDetailResponse;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderRequest;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.PayOrder.PayOrderResponse;
import com.mini.commerce.kampus.aryo.order.entity.Order;
import com.mini.commerce.kampus.aryo.order.entity.OrderItems;
import com.mini.commerce.kampus.aryo.order.enums.OrderStatus;
import com.mini.commerce.kampus.aryo.order.enums.ProductStatus;
import com.mini.commerce.kampus.aryo.order.exception.BusinessException;
import com.mini.commerce.kampus.aryo.order.mapper.OrderMapper;
import com.mini.commerce.kampus.aryo.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CatalogClientService catalogClientService;

    private final OrderMapper orderMapper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest payload) {
        Order order = new Order();
        order.setCustomerName(payload.getCustomerName());
        order.setCustomerEmail(payload.getCustomerEmail());

        List<OrderItems> orderItems = new ArrayList<>();
        for (final var item : payload.getItems()) {
            UUID productId = item.getProductId();
            var product = catalogClientService.getProductDetailById(productId);

            validateProductAvailability(product, item.getQuantity());

            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProductId(productId);
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);

            catalogClientService.reduceProductStock(productId, item.getQuantity());
        }
        order.setItems(orderItems);
        order.calculateTotalAmount();
        orderRepository.save(order);

        return orderMapper.toCreateOrderResponse(order);
    }

    public List<DetailOrderResponse> getAllOrder() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDetailOrderResponse)
                .toList();
    }

    public DetailOrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        return orderMapper.toDetailOrderResponse(order);
    }

    @Transactional
    public PayOrderResponse payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new BusinessException("Order cannot be paid");
        }

        order.setStatus(OrderStatus.PAID);

        return orderMapper.toPayOrderResponse(order);
    }

    @Transactional
    public CancelOrderResponse cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new BusinessException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (final var item : order.getItems()) {
            catalogClientService.increaseProductStock(item.getProductId(), item.getQuantity());
        }

        return orderMapper.toCancelOrderResponse(order);
    }

    private void validateProductAvailability(ProductDetailResponse product, int requestedQuantity) {
        if (product == null) {
            throw new BusinessException("Product does not exist in the catalog");
        }
        if (!product.getStatus().equals(ProductStatus.ACTIVE)) {
            throw new BusinessException("Product " + product.getName() + " is currently inactive");
        }
        if (product.getStock() < requestedQuantity) {
            throw new BusinessException(
                    "Insufficient stock for " + product.getName() + ". Available: " + product.getStock());
        }
    }
}
