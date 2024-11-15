package com.wow.libre.domain.port.out.resources;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.model.*;

import java.util.*;

public interface ObtainJsonLoader {
    PillHomeModel getResourcePillHome(String language, String transactionId);

    List<SubscriptionBenefitDto> getBenefitsPremium(String language, String transactionId);
}
