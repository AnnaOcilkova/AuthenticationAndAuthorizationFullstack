package com.ao.backend.controllers;

import com.ao.backend.exceptions.WebshopException;
import com.ao.backend.models.dto.AccessTokenDTO;
import com.ao.backend.models.dto.Dto;
import com.ao.backend.models.dto.LoginUserDTO;
import com.ao.backend.models.dto.RegisterUserDTO;
import com.ao.backend.services.AuthenticationService;
import com.ao.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

  private final UserService userService;
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<Dto> register(
      @Valid @RequestBody RegisterUserDTO user,
      BindingResult bindingResult,
      HttpServletRequest request) {
    String path = String.valueOf(request.getRequestURL());
    if (bindingResult.hasErrors())
      throw new WebshopException(HttpStatus.BAD_REQUEST, path, "Validation error.");
    return ResponseEntity.created(URI.create(path)).body(userService.saveUser(user));
  }

  @PostMapping("/login")
  public ResponseEntity<Dto> login(
      @RequestBody @Valid LoginUserDTO loginuserDTO,
      BindingResult bindingResult,
      HttpServletRequest request) {
    String path = String.valueOf(request.getRequestURL());
    if (bindingResult.hasErrors())
      throw new WebshopException(HttpStatus.BAD_REQUEST, path, "Validation error.");
    authenticationService.verifyPassword(loginuserDTO);
    User userForToken = (User) userService.loadUserByUsername(loginuserDTO.getUsername());
    String accessToken = authenticationService.generateToken(userForToken);
    return ResponseEntity.ok(new AccessTokenDTO(accessToken));
  }
}
