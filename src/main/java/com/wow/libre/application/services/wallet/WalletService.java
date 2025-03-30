package com.wow.libre.application.services.wallet;

import com.wow.libre.domain.port.in.wallet.*;
import com.wow.libre.domain.port.out.wallet.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class WalletService implements WalletPort {
    private final ObtainWallet obtainWallet;
    private final SaveWallet saveWallet;

    public WalletService(ObtainWallet obtainWallet, SaveWallet saveWallet) {
        this.obtainWallet = obtainWallet;
        this.saveWallet = saveWallet;
    }


    @Override
    public Long getPoints(Long userId, String transactionId) {
        return obtainWallet.walletByUserIdAndStatusIsTrue(userId).map(WalletEntity::getPoints).orElse(0L);

    }

    @Override
    public void addPoints(Long userId, Long points, String transactionId) {
        Optional<WalletEntity> wallet = obtainWallet.walletByUserIdAndStatusIsTrue(userId);

        WalletEntity walletEntity;
        if (wallet.isPresent()) {
            walletEntity = wallet.get();
            walletEntity.setPoints(points);
        } else {
            walletEntity = new WalletEntity();
            walletEntity.setUserId(userId);
            walletEntity.setPoints(points);
            walletEntity.setStatus(true);
        }
        saveWallet.save(walletEntity);
    }
}
