package com.wow.libre.application.services.transaction;

import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

@Service
public class TransactionService implements TransactionPort {

    private final SaveTransaction saveTransaction;
    private final ProductPort productPort;

    public TransactionService(SaveTransaction saveTransaction, ProductPort productPort) {
        this.saveTransaction = saveTransaction;
        this.productPort = productPort;
    }

    @Override
    public void save(TransactionEntity transaction, String transactionId) {
        saveTransaction.save(transaction, transactionId);
    }

    @Override
    public PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId) {
        if (transaction.isSubscription())
    }
}
