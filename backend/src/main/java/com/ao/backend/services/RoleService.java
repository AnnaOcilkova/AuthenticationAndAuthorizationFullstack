package com.ao.backend.services;

import com.ao.backend.models.Role;

import javax.servlet.http.HttpServletRequest;

public interface RoleService {
  Role saveRole(Role role, HttpServletRequest... request);
}
