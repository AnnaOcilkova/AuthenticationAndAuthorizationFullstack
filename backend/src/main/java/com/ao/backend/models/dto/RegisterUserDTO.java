package com.ao.backend.models.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
public class RegisterUserDTO implements Dto {

  @NotBlank @NotNull private String username;
  @NotBlank @NotNull private String password;
  @NotBlank @NotNull private String name;

  public RegisterUserDTO(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }
}
