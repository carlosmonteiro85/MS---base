package com.prototype.security.domain.exception;

public class UsuarioNaoPermitidoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UsuarioNaoPermitidoException(String mensagem) {
    super(mensagem);
  }

  public UsuarioNaoPermitidoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
