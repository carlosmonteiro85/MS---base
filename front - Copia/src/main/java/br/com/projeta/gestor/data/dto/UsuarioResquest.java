package br.com.projeta.gestor.data.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioResquest {
  private  Long id;
  private String cpf;
  private String nome;
  private String celular;
  private String telefone;
  private Long perfil;
  private List<Long> permissoes;
  private Long especialidade;
  private LocalDate dataNacimento;
  private String email;
  private String senha;
}
