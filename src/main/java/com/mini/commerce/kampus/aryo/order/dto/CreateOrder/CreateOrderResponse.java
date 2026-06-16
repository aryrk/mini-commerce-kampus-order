package com.mini.commerce.kampus.aryo.order.dto.CreateOrder;

import java.math.BigDecimal;
import java.util.UUID;

import com.mini.commerce.kampus.aryo.order.enums.OrderStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderResponse {
    private UUID id;
    
    @NotNull(message = "Status tidak boleh kosong")
    private OrderStatus status;

    @NotNull(message = "Total harga tidak boleh kosong")
    @Min(value = 0, message = "Total harga harus lebih besar atau sama dengan 0")
    private BigDecimal totalAmount;

}
