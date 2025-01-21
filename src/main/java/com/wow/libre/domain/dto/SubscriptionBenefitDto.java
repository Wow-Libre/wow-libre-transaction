package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
public class SubscriptionBenefitDto {
    private Long id;
    private String img;
    private String name;
    private String description;
    private String command;
    @JsonProperty("send_item")
    private Boolean sendItem;
    private Boolean reactivable;
    @JsonProperty("btn_txt")
    private String btnTxt;
    private String type;
    private Double amount;
    private List<Items> items;
    @JsonProperty("server_id")
    private Long serverId;

    @Data
    public static class Items {
        public String code;
        public Integer quantity;
    }
}
