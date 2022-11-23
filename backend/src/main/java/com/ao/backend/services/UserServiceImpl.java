package com.ao.backend.services;

import com.ao.backend.exceptions.WebshopException;
import com.ao.backend.models.AppUser;
import com.ao.backend.models.Role;
import com.ao.backend.models.dto.RegisterUserDTO;
import com.ao.backend.repositories.RoleRepository;

import com.ao.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(
          String.format("User %s not found in the database.", username));
    }
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles()
        .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), authorities);
  }

  @Override
  public AppUser getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public AppUser saveUser(RegisterUserDTO registerUserDTO) {
    verifyRegistrationCredentials(registerUserDTO.getUsername(), registerUserDTO.getPassword());
    AppUser userDb = userRepository.findByUsername(registerUserDTO.getUsername());
    if (userDb != null)
      throw new WebshopException(
          HttpStatus.UNPROCESSABLE_ENTITY, "/app/auth/register", "Username already exists.");
    String encodedPassword = bCryptPasswordEncoder.encode(registerUserDTO.getPassword());
    registerUserDTO.setPassword(encodedPassword);
    Role role = roleRepository.findByRoleName("ROLE_USER");
    AppUser user =
        new AppUser(
            null,
            registerUserDTO.getName(),
            registerUserDTO.getUsername(),
            registerUserDTO.getPassword(),
            new ArrayList<>(Collections.singletonList(role)));
    return userRepository.save(user);
  }

  @Override
  public AppUser addRoleToUser(String username, String roleName, HttpServletRequest... request) {
    String path = "";
    AppUser user = userRepository.findByUsername(username);
    if (user == null)
      throw new WebshopException(HttpStatus.NOT_FOUND, path, "User not found in the database.");
    Role role = roleRepository.findByRoleName(roleName);
    if (role == null)
      throw new WebshopException(HttpStatus.NOT_FOUND, path, "Role does not exist.");
    if (!user.getRoles().contains(role)) user.getRoles().add(role);
    else throw new WebshopException(HttpStatus.CONFLICT, path, "User already has this role.");
    return userRepository.save(user);
  }

  public void verifyRegistrationCredentials(String username, String password) {
    // Username
    // Minimum of 2 characters
    // Maximum of 20 characters
    if (username.length() < 2 || username.length() > 20)
      throw new WebshopException(
          HttpStatus.BAD_REQUEST,
          "/api/auth/register",
          "Invalid username format. Expected length of 2 - 20 characters.");

    // Password
    // Minimum of 8 characters
    // At least one uppercase letter
    // At least one number
    if (password.length() < 8)
      throw new WebshopException(
          HttpStatus.BAD_REQUEST,
          "/api/auth/register",
          "Invalid password format. Minimum length - 8 characters.");
    if (!password.matches(".*[A-Z].*"))
      throw new WebshopException(
          HttpStatus.BAD_REQUEST,
          "/api/auth/register",
          "Invalid password format. Must contain at least one uppercase character.");
    if (!password.matches(".*[0-9].*"))
      throw new WebshopException(
          HttpStatus.BAD_REQUEST,
          "/api/auth/register",
          "Invalid password format. Must contain at least one digit.");
  }
}
