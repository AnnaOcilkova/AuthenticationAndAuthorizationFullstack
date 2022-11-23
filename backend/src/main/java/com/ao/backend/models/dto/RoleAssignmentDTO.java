package com.ao.backend.models.dto;

import lombok.Data;

@Data
public class RoleAssignmentDTO implements Dto {

  private String username;
  private String roleName;
}
