package com.wow.libre.domain.dto.client;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
