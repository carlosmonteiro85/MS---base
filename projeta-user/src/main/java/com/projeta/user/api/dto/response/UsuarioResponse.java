package com.projeta.user.api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioResponse {
  private Long id;
  private String nome;
  private String celular;
  private String telefone;
  private SampleItemRequest especialidade;
  private LocalDate dataNacimento;
  private CredencialResponse credencial;
  private String avatar;
}
