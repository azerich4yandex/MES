package com.aliev.mes.interaction.receiving_order.service;

import com.aliev.mes.common.dto.position.OrderPositionDto;
import com.aliev.mes.common.dto.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.position.OrderPositionUpdateDto;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;

import java.util.List;
import java.util.UUID;

public interface OrderPositionService {

    List<OrderPositionDto> createMany(List<OrderPositionNewDto> positions);

    OrderPositionDto createOne(OrderPosition position);

    List<OrderPositionDto> getByOrderId(UUID orderId);

    List<OrderPositionDto> updateMany(List<OrderPositionUpdateDto> positions);

    OrderPositionDto updateOne(OrderPosition position);

    void deleteByOrderId(UUID orderId);
}
