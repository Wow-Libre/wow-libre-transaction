package com.wow.libre.infrastructure.repositories.jsonLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.libre.domain.model.PillHomeModel;
import com.wow.libre.domain.port.out.resources.ObtainJsonLoader;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class JsonLoader implements ObtainJsonLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonLoader.class);

  private final ObjectMapper objectMapper;
  private final Resource jsonFile;
  private Map<String, PillHomeModel> jsonPillHomeModel;

  public JsonLoader(ObjectMapper objectMapper,
                    @Value("classpath:/static/pill_home.json") Resource jsonFile) {
    this.objectMapper = objectMapper;
    this.jsonFile = jsonFile;
  }

  @PostConstruct
  public void loadJsonFile() {
    try {
      jsonPillHomeModel = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
      });
    } catch (IOException e) {
      LOGGER.error("[JsonLoader] - ERROR Message {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public PillHomeModel getResourcePillHome(String language, String transactionId) {
    return Optional.of(jsonPillHomeModel.get(language)).orElse(jsonPillHomeModel.get("es"));
  }

}
