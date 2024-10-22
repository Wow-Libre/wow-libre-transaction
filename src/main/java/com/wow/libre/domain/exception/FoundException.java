package com.wow.libre.domain.exception;

import org.springframework.http.*;

public class FoundException extends GenericErrorException {
  public FoundException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.CONFLICT);
  }
}
