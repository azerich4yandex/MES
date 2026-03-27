package com.aliev.mes.interaction.receiving.service;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderNewDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto create(OrderNewDto dto);

    OrderDto getById(UUID id);

    List<OrderDto> getAll();

    List<OrderDto> getAllByStatus(ProcessingStatus status);

    OrderDto update(OrderUpdateDto dto);

    void delete(UUID id);
}
