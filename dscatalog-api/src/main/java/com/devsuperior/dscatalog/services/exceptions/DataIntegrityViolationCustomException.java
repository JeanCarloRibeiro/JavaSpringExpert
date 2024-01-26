package com.devsuperior.dscatalog.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataIntegrityViolationCustomException extends RuntimeException {

  public DataIntegrityViolationCustomException() {
    super();
  }

  public DataIntegrityViolationCustomException(String message) {
    super(message);
  }

  public DataIntegrityViolationCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataIntegrityViolationCustomException(Throwable cause) {
    super(cause);
  }

  protected DataIntegrityViolationCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
