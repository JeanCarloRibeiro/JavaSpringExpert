package com.devsuperior.dscatalog.controllers.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationError extends StandardError {

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
