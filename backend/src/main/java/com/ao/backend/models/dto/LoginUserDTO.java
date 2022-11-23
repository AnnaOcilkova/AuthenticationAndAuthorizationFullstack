package com.ao.backend.models.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
public class LoginUserDTO implements Dto {

  @NotBlank @NotNull private String username;
  @NotBlank @NotNull private String password;

  public LoginUserDTO(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
