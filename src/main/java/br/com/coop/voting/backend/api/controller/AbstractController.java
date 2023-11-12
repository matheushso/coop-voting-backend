package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.DTO.MensagemErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractController {

    protected ResponseEntity<?> montarMensagemDeErro(HttpStatus httpStatus, String exceptionMensagem) {
        return ResponseEntity.status(httpStatus).body(new MensagemErroDTO(httpStatus.getReasonPhrase(), exceptionMensagem, httpStatus.value()));
    }
}
