package com.ao.backend.services;

import com.ao.backend.models.dto.LoginUserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
  String generateToken(UserDetails userDetails);

  boolean passwordIsCorrect(String rawPassword, String encodedPassword);

  void verifyPassword(LoginUserDTO loginuserDTO);
}
