package com.wow.libre.domain.exception;

import org.springframework.http.*;

public class InternalException extends GenericErrorException {
  public InternalException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
