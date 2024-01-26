package com.devsuperior.dscatalog.controllers.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomError {

  private LocalDateTime timestamp;
  private Integer status;
  private String error;
  private String path;

  public CustomError(LocalDateTime timestamp) {
    super();
    this.timestamp = timestamp;
  }

  public CustomError(HttpStatus status, String error, String path) {
    super();
    this.timestamp = LocalDateTime.now();
    this.status = status.value();
    this.error = error;
    this.path = path;
  }

}
