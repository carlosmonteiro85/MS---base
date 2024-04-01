package com.prototype.security.domain.exception;

public class EmailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailException(String mensagem) {
        super(mensagem);
    }

    public EmailException(String mensagem, Throwable causa) {
        super(mensagem,causa);
    }
}
