package com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.order;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position.OrderPositionDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    UUID id;
    String externalId;
    String orderNo;
    LocalDate orderDate;
    String customerName;
    ProcessingStatus status;
    List<OrderPositionDto> positions = new ArrayList<>();
}
