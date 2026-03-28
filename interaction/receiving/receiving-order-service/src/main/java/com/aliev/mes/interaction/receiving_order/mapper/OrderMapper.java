package com.aliev.mes.interaction.receiving_order.mapper;

import com.aliev.mes.common.dto.order.OrderDto;
import com.aliev.mes.common.dto.order.OrderNewDto;
import com.aliev.mes.common.dto.order.OrderUpdateDto;
import com.aliev.mes.interaction.receiving_order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "positions", ignore = true)
    OrderDto toDto(Order order);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    Order toModel(OrderNewDto dto);

    @Mapping(target = "status", ignore = true)
    Order toModel(OrderUpdateDto dto);
}
