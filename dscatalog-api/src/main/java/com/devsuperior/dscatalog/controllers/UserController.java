package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    UserDTO userDTO = this.userService.findById(id);
    return ResponseEntity.ok(userDTO);
  }

  @PostMapping
  public ResponseEntity<UserDTO> findById(@Valid @RequestBody UserInsertDTO request) {
    UserDTO userDTO = this.userService.save(request);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(userDTO.getId()).toUri();

    return ResponseEntity.created(uri).body(userDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.userService.delete(id);
    return ResponseEntity.ok().build();
  }

  //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
  @GetMapping("/me")
  public ResponseEntity<UserDTO> getMe() {
    UserDTO userDTO = this.userService.getMe();

    return ResponseEntity.ok(userDTO);
  }

}
