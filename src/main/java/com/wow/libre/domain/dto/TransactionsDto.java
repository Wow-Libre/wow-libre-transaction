package com.wow.libre.domain.dto;

import com.wow.libre.domain.model.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Data
public class TransactionsDto {
    private List<Transaction> transactions;
    private Long size;
}
