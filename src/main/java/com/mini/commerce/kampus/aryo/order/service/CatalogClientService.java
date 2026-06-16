package com.mini.commerce.kampus.aryo.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mini.commerce.kampus.aryo.order.dto.CatalogService.UpdateStock.UpdateStockRequest;
import com.mini.commerce.kampus.aryo.order.dto.CatalogService.product.ProductDetailResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogClientService {
    private final RestClient catalogRestClient;

    public ProductDetailResponse getProductDetailById(UUID productId) {
        return catalogRestClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(ProductDetailResponse.class);
    }

    @Transactional
    public void reduceProductStock(UUID productId, int quantity) {

        int currentStock = getProductDetailById(productId).getStock();
        UpdateStockRequest payload = UpdateStockRequest.builder()
                .stock(currentStock - quantity)
                .build();

        catalogRestClient.patch()
                .uri("/api/products/{id}/stock", productId)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

    @Transactional
    public void increaseProductStock(UUID productId, int quantity) {

        int currentStock = getProductDetailById(productId).getStock();
        UpdateStockRequest payload = UpdateStockRequest.builder()
                .stock(currentStock + quantity)
                .build();

        catalogRestClient.patch()
                .uri("/api/products/{id}/stock", productId)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

}
