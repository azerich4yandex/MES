package com.aliev.mes.interaction.receiving.mapper;

import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderNewDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order.OrderUpdateDto;
import com.aliev.mes.interaction.receiving.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toModel(OrderDto dto);

    @Mapping(target = "positions", ignore = true)
    OrderDto toDto(Order order);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    Order toModel(OrderNewDto dto);

    @Mapping(target = "status", ignore = true)
    Order toModel(OrderUpdateDto dto);
}
