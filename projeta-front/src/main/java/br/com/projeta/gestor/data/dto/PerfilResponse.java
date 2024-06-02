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
public class PerfilResponse {
  private Long id;
  private String descricao;
  List<SampleItemRequest> permissoes;
}
