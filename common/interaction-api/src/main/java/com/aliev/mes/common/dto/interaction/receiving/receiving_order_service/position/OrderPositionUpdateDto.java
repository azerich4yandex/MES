package com.aliev.mes.common.dto.interaction.receiving.receiving_order_service.position;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPositionUpdateDto {

    @NotNull(message = "Внешний идентификатор позиции заказа должен быть указан")
    UUID id;

    String externalId;
    Long positionNo;
    String product;
    Double quantity;


}
