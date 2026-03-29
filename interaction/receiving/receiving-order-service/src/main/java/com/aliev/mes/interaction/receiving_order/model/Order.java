package com.aliev.mes.interaction.receiving_order.model;

import com.aliev.mes.common.dto.enums.ProcessingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "received_order")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "external_id")
    String externalId;

    @Column(name = "order_no")
    String orderNo;

    @Column(name = "order_date")
    LocalDate orderDate;

    @Column(name = "customer_name")
    String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProcessingStatus status = ProcessingStatus.RECEIVED;
}
