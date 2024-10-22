package com.wow.libre.infrastructure.conf;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Data
public class Configurations {
    @Value("${application.dlocalgo.host}")
    private String dLocalGoHost;
    @Value("${application.dlocalgo.api-key}")
    private String apiKeyDLocalGoHost;
    @Value("${application.dlocalgo.api-login}")
    private String apiSecretDLocalGoHost;
}
