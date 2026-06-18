package com.mini.commerce.kampus.aryo.order.dto.DetailOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailOrderResponse {

    @NotBlank(message = "ID pesanan tidak boleh kosong")
    private UUID id;

    @NotBlank(message = "Nama pelanggan tidak boleh kosong")
    private String customerName;

    @NotBlank(message = "Email pelanggan tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String customerEmail;

    @NotBlank(message = "Status pesanan tidak boleh kosong")
    private BigDecimal totalAmount;

    @NotEmpty(message = "Daftar item tidak boleh kosong")
    @Size(min = 1, message = "Harus ada minimal 1 item dalam pesanan")
    private List<DetailOrderItemListResponse> items;
}
