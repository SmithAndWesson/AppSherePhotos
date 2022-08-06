package com.sharing.photos.demo.payload.request;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

  @NotEmpty(message = "Username cannot be empty")
  private String username;
  @NotEmpty(message = "Password cannot be empty")
  private String password;

}
