package com.devsuperior.demo.controllers.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

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

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Integer getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getPath() {
    return path;
  }
}
