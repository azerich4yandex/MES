package com.aliev.mes.common.dto.receiving.order;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    Long id;
    String externalId;
    String orderNo;
    LocalDate orderDate;
    String customerName;
    ProcessingStatus status;
    List<OrderPositionDto> positions = new ArrayList<>();
}
