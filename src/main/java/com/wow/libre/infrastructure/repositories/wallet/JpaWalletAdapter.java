package com.wow.libre.infrastructure.repositories.wallet;


import com.wow.libre.domain.port.out.wallet.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaWalletAdapter implements ObtainWallet, SaveWallet {
    private final WalletRepository walletRepository;

    public JpaWalletAdapter(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Optional<WalletEntity> walletByUserIdAndStatusIsTrue(Long userId) {
        return walletRepository.findByUserIdAndStatusIsTrue(userId);
    }

    @Override
    public void save(WalletEntity walletEntity) {
        walletRepository.save(walletEntity);
    }
}
