package com.aliev.mes.interaction.receiving_order.service.impl;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import com.aliev.mes.common.dto.position.OrderPositionDto;
import com.aliev.mes.common.dto.position.OrderPositionNewDto;
import com.aliev.mes.common.dto.position.OrderPositionUpdateDto;
import com.aliev.mes.common.exceptions.ConflictException;
import com.aliev.mes.common.exceptions.NoDataFoundException;
import com.aliev.mes.interaction.receiving_order.mapper.OrderPositionMapper;
import com.aliev.mes.interaction.receiving_order.model.OrderPosition;
import com.aliev.mes.interaction.receiving_order.repository.OrderPositionRepository;
import com.aliev.mes.interaction.receiving_order.service.OrderPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderPositionServiceImpl implements OrderPositionService {

    private final OrderPositionRepository orderPositionRepository;
    private final OrderPositionMapper orderPositionMapper;

    private record UpdateResult(OrderPosition position, boolean isChanged) {
    }

    @Override
    @Transactional
    public List<OrderPositionDto> createMany(List<OrderPositionNewDto> positions) {
        return positions.stream()
                .map(orderPositionMapper::toModel)
                .map(this::createOne)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderPositionDto createOne(OrderPosition position) {
        OrderPosition fromStorage = orderPositionRepository.getByExternalId(position.getExternalId());
        UpdateResult updateResult = new UpdateResult(fromStorage, false);
        if (fromStorage != null) {
            updateResult = updatePositionFields(fromStorage, position);
        }

        if (fromStorage == null) {
            position = orderPositionRepository.save(position);
        } else if (updateResult.isChanged) {
            position = orderPositionRepository.save(updateResult.position);
        }

        return orderPositionMapper.toDto(position);
    }

    @Override
    public List<OrderPositionDto> getByOrderId(UUID orderId) {
        List<OrderPosition> fromStorage = orderPositionRepository.getAllByReceivedOrder(orderId);
        return fromStorage.stream().map(orderPositionMapper::toDto).toList();
    }

    @Override
    @Transactional
    public List<OrderPositionDto> updateMany(List<OrderPositionUpdateDto> positions) {
        return positions.stream()
                .map(orderPositionMapper::toModel)
                .map(this::updateOne)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderPositionDto updateOne(OrderPosition position) {
        OrderPosition fromStorage = orderPositionRepository.findById(position.getId()).orElseThrow(() -> new NoDataFoundException(String.format("Позиция заказа по уникальному идентификатору %s не найдена", position.getId())));

        UpdateResult updateResult = updatePositionFields(fromStorage, position);

        if (updateResult.isChanged) {
            fromStorage = orderPositionRepository.save(fromStorage);
        }

        return orderPositionMapper.toDto(fromStorage);
    }

    @Override
    @Transactional
    public void deleteByOrderId(UUID orderId) {
        boolean hasLinkedPositions = orderPositionRepository.findAllByReceivedOrderAndStatus(orderId, ProcessingStatus.LINKED);

        if (hasLinkedPositions) {
            throw new ConflictException("Некоторые позиции заказа связаны с производством");
        }

        orderPositionRepository.deleteAllByReceivedOrder(orderId);
    }

    private UpdateResult updatePositionFields(OrderPosition fromStorage, OrderPosition fromClient) {
        boolean isChanged = false;

        if (fromClient.getReceivedOrder() != null && !fromStorage.getReceivedOrder().equals(fromClient.getReceivedOrder())) {
            fromStorage.setReceivedOrder(fromClient.getReceivedOrder());
            isChanged = true;
        }

        if (fromClient.getPositionNo() != null && !fromStorage.getPositionNo().equals(fromClient.getPositionNo())) {
            fromStorage.setPositionNo(fromClient.getPositionNo());
            isChanged = true;
        }

        if (!(fromClient.getProduct() == null) && !fromClient.getProduct().isBlank() && !fromStorage.getProduct().equals(fromClient.getProduct())) {
            fromStorage.setProduct(fromClient.getProduct());
            isChanged = true;
        }

        if (fromClient.getQuantity() != null && !fromStorage.getQuantity().equals(fromClient.getQuantity())) {
            fromStorage.setQuantity(fromClient.getQuantity());
            isChanged = true;
        }

        if (isChanged) {
            fromStorage.setStatus(ProcessingStatus.RECEIVED);
        }

        return new UpdateResult(fromStorage, isChanged);
    }
}
