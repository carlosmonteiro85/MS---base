package com.prototype.security.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
  MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
  RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
  ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
  ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
  PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
  ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
  NAO_PERMITIDO("/nao-permitido", "Requisição não autorizada"),
  RECURSO_JA_UTILIZADO("/recurso-usado", "Dados ja utilizados"),
  DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");

  private String title;
  private String uri;

  ProblemType(String path, String title) {
    this.uri = "http://localhost:8080" + path;
    this.title = title;
  }
}
