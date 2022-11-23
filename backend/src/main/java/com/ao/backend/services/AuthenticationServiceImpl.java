package com.ao.backend.services;

import com.ao.backend.exceptions.WebshopException;
import com.ao.backend.models.AppUser;
import com.ao.backend.models.dto.LoginUserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserService userService;

  @Override
  public void verifyPassword(LoginUserDTO loginuserDTO) {
    String username = loginuserDTO.getUsername();
    String password = loginuserDTO.getPassword();
    AppUser user = userService.getUserByUsername(username);
    if (user == null || !this.passwordIsCorrect(password, user.getPassword())) {
      throw new WebshopException(
          HttpStatus.NOT_FOUND, "/api/auth/login", "Invalid Username or Password.");
      // HttpStatus.valueOf(404)
    }
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    Dotenv dotenv = Dotenv.load();
    Algorithm algorithm =
        Algorithm.HMAC512(
            Objects.requireNonNull(dotenv.get("ACCESS_TOKEN_SECRET"))
                .getBytes(StandardCharsets.UTF_8));
    long milliseconds =
        System.currentTimeMillis()
            + Long.parseLong(Objects.requireNonNull(dotenv.get("ACCESS_TOKEN_EXPIRATION")));
    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withExpiresAt(new Date(milliseconds))
        .withIssuer(("/api/auth/login"))
        .withClaim(
            "roles",
            userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .sign(algorithm);
  }

  @Override
  public boolean passwordIsCorrect(String rawPassword, String encodedPassword) {
    return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
  }
}
