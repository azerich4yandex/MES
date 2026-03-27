package com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPositionDto {

    UUID id;
    String externalId;
    UUID receivedOrder;
    Long positionNo;
    String product;
    Double quantity;
    ProcessingStatus status;
}
