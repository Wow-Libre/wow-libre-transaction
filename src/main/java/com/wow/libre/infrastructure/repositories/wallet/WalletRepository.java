package com.wow.libre.infrastructure.repositories.wallet;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface WalletRepository extends CrudRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUserIdAndStatusIsTrue(Long userId);
}
