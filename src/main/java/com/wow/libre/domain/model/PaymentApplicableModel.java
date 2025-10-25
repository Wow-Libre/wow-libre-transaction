package com.wow.libre.domain.model;

import com.wow.libre.infrastructure.entities.*;

public record PaymentApplicableModel(boolean isPayment, Double amount, String currency, String orderId,
                                     String description, String tax,
                                     String returnTax, String productName, TransactionEntity transactionEntity) {
}
