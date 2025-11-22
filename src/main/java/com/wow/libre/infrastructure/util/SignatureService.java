package com.wow.libre.infrastructure.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Servicio para generar y validar signatures HMAC-SHA256
 * para comunicación segura entre aplicaciones sin usar JWT
 */
@Component
public class SignatureService {
  private static final Logger LOGGER = LoggerFactory.getLogger(SignatureService.class);
  private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

  private final String secretKey;

  public SignatureService(@Value("${application.signature.secret-key:}") String secretKey) {
    this.secretKey = secretKey;
    if (secretKey == null || secretKey.isEmpty()) {
      LOGGER.warn("Signature secret key is not configured. Signature validation will fail.");
    }
  }

  public String generateSignature(String message) {
    if (secretKey == null || secretKey.isEmpty()) {
      throw new IllegalStateException("Signature secret key is not configured");
    }

    try {
      Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
      SecretKeySpec secretKeySpec = new SecretKeySpec(
          secretKey.getBytes(StandardCharsets.UTF_8),
          HMAC_SHA256_ALGORITHM
      );
      mac.init(secretKeySpec);

      byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      LOGGER.error("Error generating signature: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to generate signature", e);
    }
  }

  public String generateSignatureForJson(String jsonBody) {
    return generateSignature(jsonBody);
  }

}

