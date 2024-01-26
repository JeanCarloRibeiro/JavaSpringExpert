package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
public class UserDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  @NotBlank(message = "Campo obrigatório!")
  private String firstName;
  @NotBlank(message = "Campo obrigatório!")
  private String lastName;
  @Email(message = "Informar um e-mail válido!")
  private String email;
  private final Set<RoleDTO> roles = new HashSet<>();

  public UserDTO() {
    super();
  }
  public UserDTO(Long id, String firstName, String lastName, String email, String password) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public UserDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
  }

}
