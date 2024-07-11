package com.devsuperior.demo.controllers.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

  private final List<FieldMessage> errors = new ArrayList<>();

  public ValidationError(HttpStatus status, String error, String path) {
    super(status, error, path);
  }

  public void addError(String fieldName, String fieldMessage) {
    this.errors.removeIf(e -> e.getFieldName().equals(fieldName));
    this.errors.add(new FieldMessage(fieldName, fieldMessage));
  }

  public List<FieldMessage> getErrors() {
    return errors;
  }

}
