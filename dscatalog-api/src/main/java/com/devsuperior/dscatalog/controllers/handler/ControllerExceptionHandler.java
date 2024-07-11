package com.devsuperior.dscatalog.controllers.handler;

import com.devsuperior.dscatalog.controllers.exceptions.CustomError;
import com.devsuperior.dscatalog.controllers.exceptions.ValidationError;
import com.devsuperior.dscatalog.services.exceptions.DataIntegrityViolationCustomException;
import com.devsuperior.dscatalog.services.exceptions.ForbiddenException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  private HttpStatus httpStatus;

  @ExceptionHandler({ResourceNotFoundException.class})
  protected ResponseEntity<CustomError> notFoundException(ResourceNotFoundException e, HttpServletRequest request) {
    httpStatus = HttpStatus.NOT_FOUND;
    ValidationError customError = new ValidationError(httpStatus, e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(httpStatus).body(customError);
  }

  @ExceptionHandler({DataIntegrityViolationCustomException.class})
  protected ResponseEntity<CustomError> dataIntegrityViolationException(DataIntegrityViolationCustomException e, HttpServletRequest request) {
    httpStatus = HttpStatus.CONFLICT;
    ValidationError customError = new ValidationError(httpStatus, e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(httpStatus).body(customError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<CustomError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError customError = new ValidationError(httpStatus, "Dados inválidos", request.getRequestURI());

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      String fieldName = error.getField();
      String msg = error.getDefaultMessage();
      customError.addError(fieldName, msg);
    }
    return ResponseEntity.status(httpStatus).body(customError);
  }

  @ExceptionHandler({ForbiddenException.class})
  protected ResponseEntity<CustomError> forbiddenException(ForbiddenException e, HttpServletRequest request) {
    httpStatus = HttpStatus.FORBIDDEN;
    ValidationError customError = new ValidationError(httpStatus, e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(httpStatus).body(customError);
  }


}
