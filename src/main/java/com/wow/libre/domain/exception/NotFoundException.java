package com.wow.libre.domain.exception;

import org.springframework.http.*;

public class NotFoundException extends GenericErrorException {
  public NotFoundException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.NOT_FOUND);
  }
}
