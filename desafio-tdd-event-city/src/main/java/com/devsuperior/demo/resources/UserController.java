package com.devsuperior.demo.resources;

import com.devsuperior.demo.dto.UserDTO;
import com.devsuperior.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
    Page<UserDTO> userDTO = this.userService.findAll(pageable);
    return ResponseEntity.ok(userDTO);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    UserDTO userDTO = this.userService.findById(id);
    return ResponseEntity.ok(userDTO);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
  @GetMapping("/me")
  public ResponseEntity<UserDTO> getMe() {
    UserDTO userDTO = this.userService.getMe();

    return ResponseEntity.ok(userDTO);
  }

}
