package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class SubscriptionBenefitDto {
    public Long id;
    public String img;
    public String name;
    public String description;
    public String command;
    @JsonProperty("send_item")
    public Boolean sendItem;
    public Boolean reactivable;
    @JsonProperty("btn_txt")
    public String btnTxt;
    public String type;
    public Double amount;
}
