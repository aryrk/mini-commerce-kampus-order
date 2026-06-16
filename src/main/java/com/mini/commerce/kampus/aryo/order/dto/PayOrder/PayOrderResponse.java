package com.mini.commerce.kampus.aryo.order.dto.PayOrder;

import java.math.BigDecimal;
import java.util.UUID;

import com.mini.commerce.kampus.aryo.order.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayOrderResponse {
    @NotNull(message = "ID pesanan tidak boleh kosong")
    private UUID id;

    @NotNull(message = "Nama pelanggan tidak boleh kosong")
    private String customerEmail;

    @NotNull(message = "Total jumlah tidak boleh kosong")
    private BigDecimal totalAmount;

    @NotNull(message = "Status pesanan tidak boleh kosong")
    private OrderStatus status;
}
