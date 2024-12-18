package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dto.EmailDTO;
import com.devsuperior.dscatalog.dto.NewPasswordDTO;
import com.devsuperior.dscatalog.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping(value = "/recover-token")
  public ResponseEntity<Void> findAll(@Validated @RequestBody EmailDTO body) {
    authService.recoveryToken(body);
    return ResponseEntity.noContent().build();
  }

  @PutMapping(value = "/new-password")
  public ResponseEntity<Void> saveNewPassword(@Validated @RequestBody NewPasswordDTO body) {
    authService.saveNewPassword(body);
    return ResponseEntity.noContent().build();
  }

}
