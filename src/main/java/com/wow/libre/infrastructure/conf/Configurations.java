package com.wow.libre.infrastructure.conf;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Data
public class Configurations {

    @Value("${application.payu.host}")
    private String payHost;
    @Value("${application.payu.api-key}")
    private String payUApiKey;
    @Value("${application.payu.api-login}")
    private String payUApiLogin;
    @Value("${application.payu.key-public}")
    private String payUApiPublic;
    @Value("${application.payu.url-confirmation}")
    private String payUConfirmUrl;


    @Value("${application.urls.wow-libre}")
    private String pathLoginWowLibre;
    @Value("${application.account.username}")
    private String loginUsername;
    @Value("${application.account.password}")
    private String loginPassword;
}
