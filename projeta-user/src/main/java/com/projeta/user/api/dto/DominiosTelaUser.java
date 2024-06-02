package com.projeta.user.api.dto;

import java.util.List;

import com.projeta.user.api.dto.response.PerfilRequest;
import com.projeta.user.api.dto.response.SampleItemRequest;

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
