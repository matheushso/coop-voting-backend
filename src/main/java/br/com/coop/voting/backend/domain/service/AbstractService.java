package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.DTO.MensagemErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractService {

    protected ResponseEntity<?> montarMensagemDeErro(HttpStatus httpStatus, String exceptionMensagem) {
        return ResponseEntity.status(httpStatus).body(new MensagemErroDTO(httpStatus.getReasonPhrase(), exceptionMensagem, httpStatus.value()));
    }
}
