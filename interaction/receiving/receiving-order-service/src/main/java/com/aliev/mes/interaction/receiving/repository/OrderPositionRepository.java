package com.aliev.mes.interaction.receiving.repository;

import com.aliev.mes.interaction.receiving.model.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, UUID> {
}
