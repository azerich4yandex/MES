package com.aliev.mes.common.dto.receiving.position;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPositionDto {

    Long id;
    String externalId;
    Long receivedOrder;
    Long positionNo;
    String product;
    Double quantity;
    ProcessingStatus status;
}
