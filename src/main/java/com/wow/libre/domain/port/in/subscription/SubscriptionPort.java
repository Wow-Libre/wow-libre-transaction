package com.wow.libre.domain.port.in.subscription;

import com.wow.libre.domain.dto.*;

public interface SubscriptionPort {

    PillWidgetHomeDto getPillHome(Long userId, String language, String transactionId);
}
