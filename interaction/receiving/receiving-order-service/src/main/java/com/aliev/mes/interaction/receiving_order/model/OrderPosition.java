package com.aliev.mes.interaction.receiving_order.model;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "received_order_position")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name = "external_id")
    String externalId;

    @Column(name = "received_order_id")
    UUID receivedOrder;

    @Column(name = "position_no")
    Long positionNo;

    @Column(name = "product")
    String product;

    @Column(name = "quantity")
    Double quantity;

    @Column(name = "status")
    ProcessingStatus status = ProcessingStatus.RECEIVED;
}
