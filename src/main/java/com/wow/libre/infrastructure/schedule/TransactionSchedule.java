package com.wow.libre.infrastructure.schedule;

import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.packages.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import jakarta.transaction.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TransactionSchedule {

    private final ObtainTransaction obtainTransaction;
    private final SaveTransaction saveTransaction;
    private final WowLibrePort wowLibrePort;
    private final PackagesPort packagesPort;

    public TransactionSchedule(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                               WowLibrePort wowLibrePort,
                               PackagesPort packagesPort) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.wowLibrePort = wowLibrePort;
        this.packagesPort = packagesPort;
    }

    @Transactional
    @Scheduled(cron = "*/10 * * * * *")
    public void sendPurchases() {
        final String transactionId = "";
        final String jwt = wowLibrePort.getJwt(transactionId);
        List<TransactionEntity> transactionEntities = obtainTransaction.findByStatusIsPaidAndSendIsFalse(transactionId);

        for (TransactionEntity transaction : transactionEntities) {
            List<ItemQuantityModel> items = packagesPort.findByProductId(transaction.getProductId(), transactionId);
            wowLibrePort.sendPurchases(jwt, transaction.getServerId(), transaction.getUserId(),
                    transaction.getAccountId(), items, transaction.getReferenceNumber(), transactionId);
            transaction.setSend(true);
            saveTransaction.save(transaction, transactionId);
        }

    }
}
