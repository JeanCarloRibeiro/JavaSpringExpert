package com.devsuperior.dscatalog.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String authority;

  public Role() {
  }

  public Role(Long id, String authority) {
    this.id = id;
    this.authority = authority;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getAuthority() {
    return authority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return authority.equals(role.authority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authority);
  }

}
