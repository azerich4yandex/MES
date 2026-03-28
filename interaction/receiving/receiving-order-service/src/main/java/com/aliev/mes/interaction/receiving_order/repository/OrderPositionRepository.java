package com.aliev.mes.interaction.receiving_order.repository;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, UUID> {

    OrderPosition getByExternalId(String externalId);

    List<OrderPosition> getAllByReceivedOrder(UUID receivedOrder);

    void deleteAllByReceivedOrder(UUID receivedOrder);

    boolean findAllByReceivedOrderAndStatus(UUID receivedOrder, ProcessingStatus status);
}
