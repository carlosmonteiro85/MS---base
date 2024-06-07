package com.prototypo.api;

public class TokenInvalidoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TokenInvalidoException(String mensagem) {
    super(mensagem);
  }

  public TokenInvalidoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}