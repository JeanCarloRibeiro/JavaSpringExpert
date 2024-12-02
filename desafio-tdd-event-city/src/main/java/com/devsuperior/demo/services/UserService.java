package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.UserDTO;
import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  public Page<UserDTO> findAll(Pageable pageable) {
    Page<User> user = userRepository.findAll(pageable);
    return user.map(UserDTO::new);
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    Optional<User> user = userRepository.findById(id);
    User entity = user.orElseThrow(() -> new ResourceNotFoundException("Entity not found..."));
    return new UserDTO(entity);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    List<UserDetailsProjection> result = this.userRepository.searchUserAndRolesByEmail(email);
    if (result.isEmpty()) {
      throw new UsernameNotFoundException("User notfound..: " + email);
    }
    User user = new User(result.get(0).getUserName(), email, result.get(0).getPassword());
    for (UserDetailsProjection r : result) {
      user.addRole(new Role(r.getRoleId(), r.getAuthority()));
    }
    return user;
  }

  @Transactional(readOnly = true)
  public UserDTO getMe() {
    User user = authenticated();
    return new UserDTO(user);
  }

  protected User authenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
    String username = jwtPrincipal.getClaim("username");
    Optional<User> result = userRepository.findByEmail(username);
    if (!result.isPresent()) {
      throw new UsernameNotFoundException("Email not found...");
    }
    return result.get();
  }

}
