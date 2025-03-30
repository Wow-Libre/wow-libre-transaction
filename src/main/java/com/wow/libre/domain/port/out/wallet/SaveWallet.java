package com.wow.libre.domain.port.out.wallet;

import com.wow.libre.infrastructure.entities.*;

public interface SaveWallet {
    void save(WalletEntity walletEntity);
}
