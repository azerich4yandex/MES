package com.aliev.mes.interaction.receiving_order.service;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.order.OrderDto;
import com.aliev.mes.common.dto.order.OrderNewDto;
import com.aliev.mes.common.dto.order.OrderUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto create(OrderNewDto dto);

    OrderDto getById(UUID id);

    List<OrderDto> getAll(List<UUID> ids, ProcessingStatus status, int from, int size);

    OrderDto update(OrderUpdateDto dto);

    void delete(UUID id);
}
