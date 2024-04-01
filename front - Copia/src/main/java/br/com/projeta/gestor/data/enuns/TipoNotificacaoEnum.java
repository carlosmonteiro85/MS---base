package br.com.projeta.gestor.data.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoNotificacaoEnum {
  
  SUCESSO("success" ), ALERTA("warning"), ERROR("error");

  @Getter
  private String descricao;
  
  public static TipoNotificacaoEnum findById(String descricao) {
      for (TipoNotificacaoEnum tipoNotificacao : values()) {
          if (tipoNotificacao.getDescricao().equals(descricao)) {
              return tipoNotificacao;
          }
      }
      return null;
  }
}
