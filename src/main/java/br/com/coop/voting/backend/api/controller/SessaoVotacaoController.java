package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.model.MensagemErro;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.service.SessaoVotacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoesvotacoes")
public class SessaoVotacaoController {

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPauta(@RequestBody SessaoVotacao sessaoVotacao) {
        try {
            sessaoVotacao = sessaoVotacaoService.cadastrarSessao(sessaoVotacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(sessaoVotacao);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErro(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemErro(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
