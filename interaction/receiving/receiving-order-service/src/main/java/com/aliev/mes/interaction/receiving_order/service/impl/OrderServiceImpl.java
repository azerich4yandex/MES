package com.aliev.mes.interaction.receiving_order.service.impl;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.receiving.order.OrderDto;
import com.aliev.mes.common.dto.receiving.order.OrderNewDto;
import com.aliev.mes.common.dto.receiving.order.OrderUpdateDto;
import com.aliev.mes.common.dto.receiving.position.OrderPositionDto;
import com.aliev.mes.common.exceptions.NoDataFoundException;
import com.aliev.mes.interaction.receiving_order.mapper.OrderMapper;
import com.aliev.mes.interaction.receiving_order.model.Order;
import com.aliev.mes.interaction.receiving_order.repository.OrderRepository;
import com.aliev.mes.interaction.receiving_order.service.OrderPositionService;
import com.aliev.mes.interaction.receiving_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderPositionService orderPositionService;

    private record UpdateResult(Order order, boolean isChanged) {
    }

    @Override
    @Transactional
    public OrderDto create(OrderNewDto dto) {
        Order order = orderMapper.toModel(dto);
        Order fromStorage = orderRepository.getByExternalId(order.getExternalId());
        UpdateResult updateResult = new UpdateResult(fromStorage, false);

        if (fromStorage != null) {
            updateResult = updateOrderFields(fromStorage, order);
        }

        if (fromStorage == null) {
            order = orderRepository.save(order);
        } else if (updateResult.isChanged) {
            order = orderRepository.save(updateResult.order);
        }

        OrderDto result = orderMapper.toDto(order);

        final Long orderId = order.getId();

        if (!dto.getPositions().isEmpty()) {
            dto.getPositions().forEach(position -> position.setReceivedOrder(orderId));

            List<OrderPositionDto> savedPositions = orderPositionService.createMany(dto.getPositions());
            result.setPositions(savedPositions);
        }

        return result;
    }

    @Override
    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoDataFoundException(String.format("Заказ по уникальному идентификатору %s не найден", id)));
        OrderDto result = orderMapper.toDto(order);
        result.setPositions(orderPositionService.getByOrderId(result.getId()));

        return result;
    }

    @Override
    public List<OrderDto> getAll(List<Long> ids, ProcessingStatus status, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        List<Order> orders;

        if (ids != null && !ids.isEmpty()) {
            orders = orderRepository.findAllById(ids);
        } else {
            if (status == ProcessingStatus.ALL) {
                orders = orderRepository.findAll(page).getContent();
            } else {
                orders = orderRepository.findAllByStatus(status, page).getContent();
            }
        }

        return orders.stream().map(order -> {
            OrderDto dto = orderMapper.toDto(order);
            dto.setPositions(orderPositionService.getByOrderId(order.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto update(OrderUpdateDto dto) {
        Order fromStorage = orderRepository.findById(dto.getId()).orElseThrow(() -> new NoDataFoundException(String.format("Заказ по уникальному идентификатору %s не найден", dto.getId())));
        Order fromClient = orderMapper.toModel(dto);
        UpdateResult updateResult = updateOrderFields(fromStorage, fromClient);

        if (updateResult.isChanged) {
            orderRepository.save(updateResult.order);
        }

        OrderDto result = orderMapper.toDto(updateResult.order);
        result.setPositions(orderPositionService.updateMany(dto.getPositions()));
        return result;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoDataFoundException(String.format("Заказ по уникальному идентификатору %s не найден", id)));
        orderPositionService.deleteByOrderId(order.getId());
        orderRepository.delete(order);
    }

    private UpdateResult updateOrderFields(Order fromStorage, Order fromClient) {
        boolean isChanged = false;

        if (!fromClient.getOrderNo().isBlank() && !fromStorage.getOrderNo().equals(fromClient.getOrderNo())) {
            fromStorage.setOrderNo(fromClient.getOrderNo());
            isChanged = true;
        }

        if (!(fromClient.getOrderDate() == null) && !fromStorage.getOrderDate().equals(fromClient.getOrderDate())) {
            fromStorage.setOrderDate(fromClient.getOrderDate());
            isChanged = true;
        }

        if (!fromClient.getCustomerName().isBlank() && !fromStorage.getCustomerName().equals(fromClient.getCustomerName())) {
            fromStorage.setCustomerName(fromClient.getCustomerName());
            isChanged = true;
        }

        if (isChanged) {
            fromStorage.setStatus(ProcessingStatus.RECEIVED);
            log.debug("Заказ был изменён");
        }

        return new UpdateResult(fromStorage, isChanged);
    }
}
