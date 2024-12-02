package com.devsuperior.demo.resources.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

  private final List<FieldMessage> errors = new ArrayList<>();

  public ValidationError() {
    super();
  }

  public void addError(String fieldName, String fieldMessage) {
    this.errors.removeIf(e -> e.getFieldName().equals(fieldName));
    this.errors.add(new FieldMessage(fieldName, fieldMessage));
  }

  public List<FieldMessage> getErrors() {
    return errors;
  }

}
