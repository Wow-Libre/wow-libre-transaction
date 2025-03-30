package com.wow.libre.domain.port.out.wallet;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainWallet {
    Optional<WalletEntity> walletByUserIdAndStatusIsTrue(Long userId);
}
