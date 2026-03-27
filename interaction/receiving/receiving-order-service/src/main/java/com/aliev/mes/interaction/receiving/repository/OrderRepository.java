package com.aliev.mes.interaction.receiving.repository;

import com.aliev.mes.interaction.receiving.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
