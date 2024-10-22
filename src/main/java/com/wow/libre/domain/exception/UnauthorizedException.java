package com.wow.libre.domain.exception;

import org.springframework.http.*;

public class UnauthorizedException extends GenericErrorException {
  public UnauthorizedException(String message, String transactionId) {
    super(transactionId, message, HttpStatus.UNAUTHORIZED);
  }
}
