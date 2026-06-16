package com.mini.commerce.kampus.aryo.order.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(nullable = false, name = "order_id")
    @ManyToOne
    private Order order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    @Min(value = 1, message = "Harga harus lebih besar dari 0")
    private BigDecimal productPrice;

    @Column(nullable = false)
    @Min(value = 1, message = "Kuantitas minimal 1")
    private int quantity;
}
