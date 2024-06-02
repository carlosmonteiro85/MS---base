package com.projeta.user.api.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotNull
  private String nome;
  @NotNull
  private String cpf;
  @NotNull
  private String email;
  @NotNull
  private String password;
  @NotNull
  private List<Long> perfils;
}
