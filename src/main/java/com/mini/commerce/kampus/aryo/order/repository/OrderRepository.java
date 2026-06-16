package com.mini.commerce.kampus.aryo.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mini.commerce.kampus.aryo.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}
