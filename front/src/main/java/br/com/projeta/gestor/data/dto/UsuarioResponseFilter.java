package br.com.projeta.gestor.data.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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