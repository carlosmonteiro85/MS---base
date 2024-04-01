package com.prototype.security.api.dto;

import java.time.LocalDate;
import java.util.List;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioResquest {
  @NonNull
  private String cpf;
  @NonNull
  private String nome;
  private String celular;
  private String telefone;
  private Long perfil;
  private List<Long> permissoes;
  private Long especialidade;
  private LocalDate dataNacimento;
  @NonNull
  private String email;
  private String avatar;
}
