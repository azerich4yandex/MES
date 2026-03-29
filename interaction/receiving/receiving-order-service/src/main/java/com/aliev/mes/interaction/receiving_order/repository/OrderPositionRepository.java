package com.aliev.mes.interaction.receiving_order.repository;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {

    OrderPosition getByExternalId(String externalId);

    List<OrderPosition> getAllByReceivedOrder(Long receivedOrder);

    void deleteAllByReceivedOrder(Long receivedOrder);

    boolean findAllByReceivedOrderAndStatus(Long receivedOrder, ProcessingStatus status);
}
