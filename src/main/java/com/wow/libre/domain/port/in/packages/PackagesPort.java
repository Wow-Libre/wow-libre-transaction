package com.wow.libre.domain.port.in.packages;


import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface PackagesPort {
    List<ItemQuantityModel> findByProductId(ProductEntity product, String transactionId);
}
