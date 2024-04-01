package br.com.projeta.gestor.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DominiosTelaUser {
  private Long idUsuarioLogado;
  private List<SampleItemRequest> especialidades;
  private List<PerfilRequest> perfils;
}
