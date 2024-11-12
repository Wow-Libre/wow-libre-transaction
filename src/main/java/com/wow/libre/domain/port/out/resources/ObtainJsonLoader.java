package com.wow.libre.domain.port.out.resources;

import com.wow.libre.domain.model.*;

public interface ObtainJsonLoader {
    PillHomeModel getResourcePillHome(String language, String transactionId);
}
