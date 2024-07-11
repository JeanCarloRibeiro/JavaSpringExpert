package com.devsuperior.demo.dto;

import com.devsuperior.demo.services.validation.UserUpdateValid;

import java.io.Serial;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
  @Serial
  private static final long serialVersionUID = 1L;

}
