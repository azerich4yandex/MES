package com.aliev.mes.interaction.receiving_order.controller;


import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.order.OrderDto;
import com.aliev.mes.common.dto.order.OrderNewDto;
import com.aliev.mes.common.dto.order.OrderUpdateDto;
import com.aliev.mes.common.feign.ReceivingOrderFeignClient;
import com.aliev.mes.interaction.receiving_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.aliev.mes.common.util.Constants.RECEIVING_ORDER;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(RECEIVING_ORDER)
public class ReceivingOrderController implements ReceivingOrderFeignClient {

    private final OrderService orderService;


    @Override
    public ResponseEntity<OrderDto> add(OrderNewDto dto) {
        return new ResponseEntity<>(orderService.create(dto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderDto>> getAll(List<UUID> ids, ProcessingStatus status, int from, int size) {
        return new ResponseEntity<>(orderService.getAll(ids, status == null ? ProcessingStatus.ALL : status, from, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDto> getById(UUID id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDto> update(OrderUpdateDto dto) {
        return new ResponseEntity<>(orderService.update(dto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
