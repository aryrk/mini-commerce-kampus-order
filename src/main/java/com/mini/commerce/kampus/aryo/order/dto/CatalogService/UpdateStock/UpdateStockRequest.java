package com.mini.commerce.kampus.aryo.order.dto.CatalogService.UpdateStock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStockRequest {
    @NotNull(message = "Stock tidak boleh kosong")
    @Min(value = 0, message = "Stock tidak boleh negatif")
    private int stock;
}
