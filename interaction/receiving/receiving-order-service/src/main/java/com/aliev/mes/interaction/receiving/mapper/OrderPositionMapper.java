package com.aliev.mes.interaction.receiving.mapper;

import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionUpdateDto;
import com.aliev.mes.interaction.receiving.model.OrderPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderPositionMapper {

    OrderPosition toModel(OrderPositionDto dto);

    OrderPositionDto toDto(OrderPosition orderPosition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderPosition toModel(OrderPositionNewDto dto);

    @Mapping(target = "receivedOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderPosition toModel(OrderPositionUpdateDto dto);
}
