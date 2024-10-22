package com.wow.libre.domain.exception;

import org.springframework.http.*;

public class BadRequestException extends GenericErrorException {
  public BadRequestException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.BAD_REQUEST);
  }
}
