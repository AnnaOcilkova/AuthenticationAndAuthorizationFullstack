package com.ao.backend.controllers;

import com.ao.backend.models.Role;
import com.ao.backend.models.dto.Dto;
import com.ao.backend.models.dto.RoleAssignmentDTO;
import com.ao.backend.services.RoleService;
import com.ao.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class RoleController {

  private final UserService userService;
  private final RoleService roleService;

  @PostMapping("/roles")
  public ResponseEntity<Dto> saveRole(
      @RequestBody Role role, HttpServletRequest httpServletRequest) {
    String path = httpServletRequest.getRequestURI();
    return ResponseEntity.created(URI.create(path)).body(roleService.saveRole(role));
  }

  @PostMapping("/roles/assign")
  public ResponseEntity<Dto> assignRole(@RequestBody RoleAssignmentDTO form) {
    return ResponseEntity.ok(userService.addRoleToUser(form.getUsername(), form.getRoleName()));
  }
}
