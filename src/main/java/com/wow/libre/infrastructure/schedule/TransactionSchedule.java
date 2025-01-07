package com.wow.libre.infrastructure.schedule;

import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.packages.*;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSchedule.class);

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
    @Scheduled(cron = "1/50 * * * * *")
    public void sendPurchases() {
        final String transactionId = "";
        List<TransactionEntity> transactionEntities = obtainTransaction.findByStatusIsPaidAndSendIsFalse(transactionId);


        for (TransactionEntity transaction : transactionEntities) {
            try {
                final String jwt = wowLibrePort.getJwt(transactionId);

                List<ItemQuantityModel> items = packagesPort.findByProductId(transaction.getProductId(), transactionId);
                Double amount = transaction.isGold() ? transaction.getPrice() : 0d;
                wowLibrePort.sendPurchases(jwt, transaction.getServerId(), transaction.getUserId(),
                        transaction.getAccountId(), amount, items, transaction.getReferenceNumber(), transactionId);
                transaction.setSend(true);
                if (transaction.isGold()) {
                    transaction.setStatus(TransactionStatus.DELIVERED.getType());
                }
                saveTransaction.save(transaction, transactionId);
            } catch (Exception e) {
                LOGGER.error("Error Transaction Sends {}", e.getLocalizedMessage());
            }
        }


    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void refreshJwt() {
        wowLibrePort.getJwt("Internal Refresh Token");
    }
}
