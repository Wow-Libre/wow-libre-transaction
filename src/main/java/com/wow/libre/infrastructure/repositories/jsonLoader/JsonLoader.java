package com.wow.libre.infrastructure.repositories.jsonLoader;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.out.resources.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class JsonLoader implements ObtainJsonLoader {
    private final ObjectMapper objectMapper;
    private final Resource jsonFile;
    private final Resource jsonFileBenefitsPremium;
    private Map<String, PillHomeModel> jsonPillHomeModel;
    private Map<String, List<SubscriptionBenefitDto>> jsonBenefitsPremium;

    public JsonLoader(ObjectMapper objectMapper,
                      @Value("classpath:/static/pill_home.json") Resource jsonFile,
                      @Value("classpath:/static/benefits_premium.json") Resource jsonFileBenefitsPremium) {
        this.objectMapper = objectMapper;
        this.jsonFile = jsonFile;
        this.jsonFileBenefitsPremium = jsonFileBenefitsPremium;
    }

    @PostConstruct
    public void loadJsonFile() {
        try {
            jsonPillHomeModel = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
            });
            jsonBenefitsPremium = objectMapper.readValue(jsonFileBenefitsPremium.getInputStream(),
                    new TypeReference<>() {
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PillHomeModel getResourcePillHome(String language, String transactionId) {
        return Optional.of(jsonPillHomeModel.get(language)).orElse(jsonPillHomeModel.get("es"));
    }

    @Override
    public List<SubscriptionBenefitDto> getBenefitsPremium(String language, String transactionId) {
        return Optional.of(jsonBenefitsPremium.get(language)).orElse(jsonBenefitsPremium.get("es"));
    }
}
