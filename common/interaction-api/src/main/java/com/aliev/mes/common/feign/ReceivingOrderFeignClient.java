package com.aliev.mes.common.feign;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.order.OrderDto;
import com.aliev.mes.common.dto.order.OrderNewDto;
import com.aliev.mes.common.dto.order.OrderUpdateDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.aliev.mes.common.util.Constants.ID;
import static com.aliev.mes.common.util.Constants.RECEIVING_ORDER;

@FeignClient(name = "receiving-order-service", path = RECEIVING_ORDER)
public interface ReceivingOrderFeignClient {

    @PostMapping
    ResponseEntity<OrderDto> add(@Valid @RequestBody OrderNewDto dto);

    @GetMapping
    ResponseEntity<List<OrderDto>> getAll(
            @RequestParam(name = "ids", required = false) List<UUID> ids,
            @RequestParam(name = "status", required = false) ProcessingStatus status,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size);

    @GetMapping(ID)
    ResponseEntity<OrderDto> getById(@PathVariable UUID id);

    @PatchMapping(ID)
    ResponseEntity<OrderDto> update(@Valid @RequestBody OrderUpdateDto dto);

    @DeleteMapping(ID)
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
