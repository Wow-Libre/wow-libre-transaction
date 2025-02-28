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

    @Bean("product-reference")
    public RandomString productRandomString() {
        return new RandomString(30, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("subscription-reference")
    public RandomString subscriptionRandomString() {
        return new RandomString(40, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

}
