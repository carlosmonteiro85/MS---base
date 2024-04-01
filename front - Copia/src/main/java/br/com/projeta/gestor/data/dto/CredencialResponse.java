package br.com.projeta.gestor.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredencialResponse {
	private Long id;
	private String username;
	private String cpf;
	private String email;
	private String password;
  private boolean ativo;
  private List<PerfilResponse> perfils;
}
