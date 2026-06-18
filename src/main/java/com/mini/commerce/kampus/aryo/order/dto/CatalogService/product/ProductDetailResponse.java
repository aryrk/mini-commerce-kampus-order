package com.mini.commerce.kampus.aryo.order.dto.CatalogService.product;

import java.math.BigDecimal;
import java.util.UUID;

import com.mini.commerce.kampus.aryo.order.enums.ProductStatus;

import lombok.Data;

@Data
public class ProductDetailResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private ProductStatus status;
}
