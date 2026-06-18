package com.mini.commerce.kampus.aryo.order.dto.CreateOrder;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderItemListRequest {
    @NotNull(message = "Product ID tidak boleh kosong")
    private UUID productId;
    @NotNull(message = "Kuantitas tidak boleh kosong")
    @Min(value = 1, message = "Kuantitas minimal 1")
    private Integer quantity;
}
