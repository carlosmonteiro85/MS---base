package com.projeta.user.domain.exception;

public class TokenExceprion extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TokenExceprion(String mensagem) {
        super(mensagem);
    }

    public TokenExceprion(String mensagem, Throwable causa) {
        super(mensagem,causa);
    }
}
