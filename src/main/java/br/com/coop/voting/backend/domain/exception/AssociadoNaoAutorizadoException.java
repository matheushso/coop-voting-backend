package br.com.coop.voting.backend.domain.exception;

public class AssociadoNaoAutorizadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssociadoNaoAutorizadoException(String message) {
		super(message);
	}
}
