package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataIntegrityViolationCustomException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private AuthService authService;

  @Transactional(readOnly = true)
  public Page<UserDTO> findAll(Pageable pageable) {
    Page<User> user = this.userRepository.findAll(pageable);
    return user.map(UserDTO::new);
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    Optional<User> user = this.userRepository.findById(id);
    User entity = user.orElseThrow(() -> new ResourceNotFoundException("Entity not found..."));
    return new UserDTO(entity);
  }

  @Transactional(readOnly = true)
  public UserDTO findMe() {
    User user = authService.authenticated();
    return new UserDTO(user);
  }

  @Transactional
  public UserDTO save(UserInsertDTO dto) {

    Optional<User> byEmail = this.userRepository.findByEmail(dto.getEmail());
    if (byEmail.isPresent()) {
      throw new DataIntegrityViolationCustomException("Email já cadastrado!");
    }
    User entity = new User();
    copyDtoToEntity(dto, entity);
    entity.getRoles().clear(); //Limpa as roles vindas do json para considerar somente a ROLE_OPERATOR

    Optional<Role> operator = roleRepository.findByAuthority("ROLE_OPERATOR");
    Role role = operator.orElseThrow(() -> new ResourceNotFoundException("ROLE_OPERATOR not found."));
    entity.getRoles().add(role);

    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    entity = this.userRepository.save(entity);
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserUpdateDTO dto) {
    try {
      User entity = this.userRepository.getReferenceById(id);
      copyDtoToEntity(dto, entity);
      entity = this.userRepository.save(entity);
      return new UserDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + 1);
    }
  }

  @Transactional
  public void delete(Long id) {
    try {
      User entity = this.userRepository.getReferenceById(id);
      this.userRepository.delete(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + 1);
    }
  }

  private void copyDtoToEntity(UserDTO dto, User entity) {
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setEmail(dto.getEmail());
    entity.getRoles().clear();

    dto.getRoles().forEach(roleDTO -> {
      Role role = this.roleRepository.getReferenceById(roleDTO.getId());
      entity.getRoles().add(role);
    });
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

}
