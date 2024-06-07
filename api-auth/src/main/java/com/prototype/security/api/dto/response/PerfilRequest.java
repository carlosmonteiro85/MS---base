package com.prototype.security.api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilRequest {
  private Long id;
  private String descricao;
  List<SampleItemRequest> permissoes;
}
