package com.wow.libre.infrastructure.conf;

import com.wow.libre.infrastructure.util.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
public class ConfigRandomSerial {
    @Bean("random-string")
    public RandomString configRandomString() {
        return new RandomString(20, "abcdefghijklmnopqrstuvwxyz0123456789");
    }


}
