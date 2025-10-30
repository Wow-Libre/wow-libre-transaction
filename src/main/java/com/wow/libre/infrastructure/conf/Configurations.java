package com.wow.libre.infrastructure.conf;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Data
public class Configurations {
    @Value("${application.urls.wow-libre}")
    private String pathLoginWowLibre;
    @Value("${application.urls.payu}")
    private String pathPayU;
}
