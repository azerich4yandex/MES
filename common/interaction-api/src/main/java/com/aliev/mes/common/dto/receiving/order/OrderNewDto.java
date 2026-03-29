package com.aliev.mes.common.dto.receiving.order;

import com.aliev.mes.common.dto.receiving.position.OrderPositionNewDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderNewDto {

    @NotBlank(message = "Внешний идентификатор заказа должен быть указан")
    @Size(min = 1, max = 255, message = "Длина внешнего идентификатор не может быть меньше 1 символа и больше 255")
    String externalId;

    @NotBlank(message = "Номер заказа должен быть указан")
    String orderNo;

    @NotNull(message = "Дата заказа должна быть указана")
    LocalDate orderDate;

    @NotBlank(message = "Наименование заказчика должно быть указано")
    @Size(min = 1, max = 255, message = "Длина наименования заказчика не может быть меньше 1 символа и больше 255")
    String customerName;

    List<OrderPositionNewDto> positions = new ArrayList<>();
}
