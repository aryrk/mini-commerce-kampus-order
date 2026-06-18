package com.mini.commerce.kampus.aryo.order.mapper;

import org.mapstruct.Mapper;

import com.mini.commerce.kampus.aryo.order.dto.CancelOrder.CancelOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.CreateOrder.CreateOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderResponse;
import com.mini.commerce.kampus.aryo.order.dto.PayOrder.PayOrderResponse;
import com.mini.commerce.kampus.aryo.order.entity.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface OrderMapper {
    CancelOrderResponse toCancelOrderResponse(Order order);
    CreateOrderResponse toCreateOrderResponse(Order order);
    DetailOrderResponse toDetailOrderResponse(Order order);
    PayOrderResponse toPayOrderResponse(Order order);
}
