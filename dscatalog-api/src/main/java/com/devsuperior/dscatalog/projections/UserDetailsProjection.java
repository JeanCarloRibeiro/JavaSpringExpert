package com.devsuperior.dscatalog.projections;

public interface UserDetailsProjection {

  String getUserName();
  String getPassword();
  Long getRoleId();
  String getAuthority();

}
