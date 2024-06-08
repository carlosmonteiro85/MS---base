package com.projeta.user.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestorePasswordRequest {
  @NotNull
  private String password;
  @NotNull
  private String token;
}
