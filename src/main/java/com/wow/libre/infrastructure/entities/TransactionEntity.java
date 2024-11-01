package com.wow.libre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.time.*;

@Data
@Entity
@Table(name = "transaction")
public class TransactionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "server_id")
    private Long serverId;
    private Double price;
    private String status;
    @JoinColumn(
            name = "subscription_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private SubscriptionEntity subscriptionId;
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private ProductEntity productId;
    @Column(name = "reference_number")
    private String referenceNumber;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "gold")
    private boolean gold;
    private String currency;
    private boolean send;
}
