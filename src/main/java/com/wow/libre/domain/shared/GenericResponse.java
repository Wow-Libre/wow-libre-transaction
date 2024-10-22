package com.wow.libre.domain.shared;

import lombok.*;

@Data
public class GenericResponse<D> {
    private Integer code;
    private String message;
    private String transactionId;
    private D data;
}
