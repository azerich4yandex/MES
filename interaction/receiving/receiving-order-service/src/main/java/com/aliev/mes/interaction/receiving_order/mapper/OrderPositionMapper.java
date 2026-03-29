package com.aliev.mes.interaction.receiving_order.mapper;

import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionUpdateDto;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderPositionMapper {

    OrderPositionDto toDto(OrderPosition orderPosition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderPosition toModel(OrderPositionNewDto dto);

    @Mapping(target = "receivedOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderPosition toModel(OrderPositionUpdateDto dto);
}
