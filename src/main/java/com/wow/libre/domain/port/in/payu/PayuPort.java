package com.wow.libre.domain.port.in.payu;

import com.wow.libre.domain.dto.payu.*;

public interface PayuPort {
    PayUOrderDetailResponse getOrderDetailByReference(String referenceCode, String apiLogin,
                                                      String apiKey);
}
