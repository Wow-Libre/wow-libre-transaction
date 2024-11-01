package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import com.wow.libre.domain.model.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public class CreateTransactionItemsDto {
    @JsonProperty("server_id")
    private Long serverId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("account_id")
    private Long accountId;
    private String reference;
    @NotNull
    private List<ItemQuantityModel> items;
}
