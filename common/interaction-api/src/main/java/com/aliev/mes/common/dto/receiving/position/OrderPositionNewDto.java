package com.aliev.mes.common.dto.receiving.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPositionNewDto {

    @NotBlank(message = "Внешний идентификатор позиции заказа должен быть указан")
    String externalId;

    Long receivedOrder;

    @NotNull(message = "Номер позиции заказа должен быть указан")
    @Positive(message = "Номер позиции заказа должен быть больше 0")
    Long positionNo;

    @NotBlank(message = "Наименование продукта должно быть указано")
    String product;

    @Positive(message = "Количество товара должно быть больше 0")
    Double quantity;
}
