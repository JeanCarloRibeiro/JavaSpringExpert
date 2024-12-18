package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.EmailDTO;
import com.devsuperior.dscatalog.dto.NewPasswordDTO;
import com.devsuperior.dscatalog.entities.PasswordRecover;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.PasswordRecoverRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class AuthService {

  @Value("${email.password-recover.token.minutes}")
  private Long tokenMinutes;
  @Value("${email.password-recover.uri}")
  private String urlRecoverUri;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordRecoverRepository passwordRepository;
  @Autowired
  private EmailService emailService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public void recoveryToken(EmailDTO body) {
    Optional<User> email = userRepository.findByEmail(body.getEmail());
    User user = email.orElseThrow(() -> new ResourceNotFoundException("Email não encontrado."));

    PasswordRecover password = new PasswordRecover();
    password.setEmail(user.getEmail());
    password.setToken(UUID.randomUUID().toString());
    password.setExpiration(Instant.now().plus(tokenMinutes, MINUTES));
    passwordRepository.save(password);

    String bodyMsg = "Acesse o link para definir uma nova senha\n\n" +
            urlRecoverUri + password.getToken() + ", Validade de " + tokenMinutes + " Minutos.";

    emailService.sendEmail(body.getEmail(), "Recuperação de Senha", bodyMsg);
  }

  public void saveNewPassword(NewPasswordDTO body) {
    List<PasswordRecover> result = passwordRepository.
            searchValidToken(body.getToken(), Instant.now());

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Token inválido");
    }
    Optional<User> userOpt = userRepository.findByEmail(result.get(0).getEmail());
    User user = userOpt.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    user.setPassword(passwordEncoder.encode(body.getPassword()));
    userRepository.save(user);
  }

  protected User authenticated() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
      String username = jwtPrincipal.getClaim("username");
      return userRepository.findByEmail(username).orElseThrow();
    } catch (Exception e) {
      throw new UsernameNotFoundException("Invalid user");
    }

  }

}
