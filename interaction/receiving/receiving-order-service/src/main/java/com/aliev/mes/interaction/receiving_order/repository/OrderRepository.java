package com.aliev.mes.interaction.receiving_order.repository;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.interaction.receiving_order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order getByExternalId(String externalId);

    Page<Order> findAllByStatus(ProcessingStatus status, Pageable pageable);
}
