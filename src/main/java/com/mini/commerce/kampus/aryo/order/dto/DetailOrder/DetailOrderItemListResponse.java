package com.mini.commerce.kampus.aryo.order.dto.DetailOrder;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailOrderItemListResponse {
    @NotBlank(message = "ID item tidak boleh kosong")
    private UUID id;
    @NotBlank(message = "Product ID tidak boleh kosong")
    private UUID productId;
    @NotBlank(message = "Nama produk tidak boleh kosong")
    private String productName;
    @NotNull(message = "Harga produk tidak boleh kosong")
    private BigDecimal productPrice;
    @NotNull(message = "Kuantitas tidak boleh kosong")
    @Min(value = 1, message = "Kuantitas minimal 1")
    private Integer quantity;
}
