package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Data
public class LoginResponse {
    public String jwt;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("expiration_date")
    public Date expirationDate;
    @JsonProperty("avatar_url")
    public String avatarUrl;
    public String language;
}
