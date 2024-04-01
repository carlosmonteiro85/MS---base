package com.prototype.security.domain.model.enuns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoPermissaoEnum {
  READ("Visualizar"),
  CREATE("Criar"), 
  UPDATE("Atualizar"), 
  DELETE("Deletar"); 

  @Getter
  private final String descricao;
}
