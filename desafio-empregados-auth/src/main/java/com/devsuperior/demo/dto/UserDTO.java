package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  @NotBlank(message = "Campo obrigatório!")
  private String name;
  @Email(message = "Informar um e-mail válido!")
  private String email;
  private final Set<RoleDTO> roles = new HashSet<>();

  public UserDTO() {
    super();
  }
  public UserDTO(Long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public UserDTO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<RoleDTO> getRoles() {
    return roles;
  }

}
