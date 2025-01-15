package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil customUserUtil;

	private UserEntity user;
	private String existingUser;
	private String nonExistingUser;

	@BeforeEach
	void setUp() {
		user = UserFactory.createUserEntity();
		List<UserDetailsProjection> userAdminProjection = UserDetailsFactory.createCustomAdminUser("maria@gmail.com");
		existingUser = "maria@gmail.com";
		nonExistingUser = "pedro@gmail.com";

		Mockito.when(repository.findByUsername(existingUser)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByUsername(nonExistingUser)).thenReturn(Optional.empty());

		Mockito.when(repository.searchUserAndRolesByUsername(existingUser)).thenReturn(userAdminProjection);
		Mockito.when(repository.searchUserAndRolesByUsername(nonExistingUser)).thenReturn(List.of());
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(user.getUsername());

		UserEntity result = service.authenticated();
		Assertions.assertNotNull(result);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.when(customUserUtil.getLoggedUsername()).thenThrow(ClassCastException.class);

		Assertions.assertThrows(UsernameNotFoundException.class, () -> service.authenticated());
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUser);
		Assertions.assertNotNull(result);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, ()-> service.loadUserByUsername(nonExistingUser));
	}
}
