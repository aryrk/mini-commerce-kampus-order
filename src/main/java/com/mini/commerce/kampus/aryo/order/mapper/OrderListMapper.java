package com.mini.commerce.kampus.aryo.order.mapper;

import org.mapstruct.Mapper;

import com.mini.commerce.kampus.aryo.order.dto.DetailOrder.DetailOrderItemListResponse;
import com.mini.commerce.kampus.aryo.order.entity.OrderItems;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface OrderListMapper {
    DetailOrderItemListResponse toDetailOrderItemListResponse(OrderItems orderItems);
}
