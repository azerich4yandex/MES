package com.aliev.mes.common.dto.receiving.order;

import com.aliev.mes.common.dto.receiving.position.OrderPositionUpdateDto;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateDto {

    @NotNull(message = "Внутренний идентификатор заказа должен быть указан")
    Long id;

    String externalId;
    String orderNo;
    LocalDate orderDate;
    String customerName;
    List<OrderPositionUpdateDto> positions = new ArrayList<>();
}
