package com.aliev.mes.interaction.receiving_order.service;

import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionUpdateDto;

import java.util.List;

public interface OrderPositionService {

    List<OrderPositionDto> createMany(List<OrderPositionNewDto> positions);

    OrderPositionDto createOne(OrderPositionNewDto position);

    List<OrderPositionDto> getByOrderId(Long orderId);

    List<OrderPositionDto> updateMany(List<OrderPositionUpdateDto> positions);

    OrderPositionDto updateOne(OrderPositionUpdateDto position);

    void deleteByOrderId(Long orderId);
}
