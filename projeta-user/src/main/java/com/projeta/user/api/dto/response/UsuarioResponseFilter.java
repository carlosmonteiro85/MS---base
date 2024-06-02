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
public class UsuarioResponseFilter {
  private Long id;
  private String nome;
  private String celular;
  private String telefone;
  private String especialidade;
  private LocalDate dataNacimento;
	private String username;
	private String cpf;
	private String email;
  private String perfil;
}
