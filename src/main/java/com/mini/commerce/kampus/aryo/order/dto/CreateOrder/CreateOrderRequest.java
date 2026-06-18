package com.mini.commerce.kampus.aryo.order.dto.CreateOrder;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Nama pelanggan tidak boleh kosong")
    private String customerName;

    @NotBlank(message = "Email pelanggan tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String customerEmail;

    @Valid
    @NotEmpty(message = "Daftar item tidak boleh kosong")
    @Size(min = 1, message = "Harus ada minimal 1 item dalam pesanan")
    private List<CreateOrderItemListRequest> items;
}
