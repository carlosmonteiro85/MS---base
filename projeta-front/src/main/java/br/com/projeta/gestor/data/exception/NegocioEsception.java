package br.com.projeta.gestor.data.exception;

public class NegocioEsception extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NegocioEsception(String mensagem) {
    super(mensagem);
  }

  public NegocioEsception(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
