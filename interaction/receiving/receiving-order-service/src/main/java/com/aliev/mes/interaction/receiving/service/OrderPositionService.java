package com.aliev.mes.interaction.receiving.service;

import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderPositionService {

    OrderPositionDto create(OrderPositionNewDto dto);

    OrderPositionDto getById(UUID uuid);

    List<OrderPositionDto> getByOrderId(UUID orderId);

    OrderPositionDto update(OrderPositionUpdateDto dto);

    void delete(UUID id);

    void deleteByOrderId(UUID orderId);
}
