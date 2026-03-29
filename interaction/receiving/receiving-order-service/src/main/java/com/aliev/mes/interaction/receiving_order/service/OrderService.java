package com.aliev.mes.interaction.receiving_order.service;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.receiving.order.OrderDto;
import com.aliev.mes.common.dto.receiving.order.OrderNewDto;
import com.aliev.mes.common.dto.receiving.order.OrderUpdateDto;

import java.util.List;

public interface OrderService {

    OrderDto create(OrderNewDto dto);

    OrderDto getById(Long id);

    List<OrderDto> getAll(List<Long> ids, ProcessingStatus status, int from, int size);

    OrderDto update(OrderUpdateDto dto);

    void delete(Long id);
}
