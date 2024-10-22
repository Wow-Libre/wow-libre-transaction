package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
}
