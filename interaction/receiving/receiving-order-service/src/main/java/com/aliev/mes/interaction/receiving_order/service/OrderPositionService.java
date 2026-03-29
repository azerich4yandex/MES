package com.aliev.mes.interaction.receiving_order.service;

import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionUpdateDto;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;

import java.util.List;

public interface OrderPositionService {

    List<OrderPositionDto> createMany(List<OrderPositionNewDto> positions);

    OrderPositionDto createOne(OrderPosition position);

    List<OrderPositionDto> getByOrderId(Long orderId);

    List<OrderPositionDto> updateMany(List<OrderPositionUpdateDto> positions);

    OrderPositionDto updateOne(OrderPosition position);

    void deleteByOrderId(Long orderId);
}
